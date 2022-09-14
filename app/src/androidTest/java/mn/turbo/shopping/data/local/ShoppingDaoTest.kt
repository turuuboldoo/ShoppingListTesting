package mn.turbo.shopping.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import mn.turbo.shopping.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        database = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ShoppingItemDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        dao = database.shoppingDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runTest {
        val shoppingItem = ShoppingItem(
            "name",
            1,
            1f,
            "url",
            id = 1
        )
        dao.insertShoppingItem(shoppingItem)

        val shoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(shoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = ShoppingItem(
            "name",
            1,
            1f,
            "url",
            id = 1
        )
        dao.insertShoppingItem(shoppingItem)

        dao.deleteShoppingItem(shoppingItem)

        val shoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(shoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runTest {
        val shoppingItem1 = ShoppingItem("name", 2, 10f, "url")
        val shoppingItem2 = ShoppingItem("name", 4, 5.5f, "url")
        val shoppingItem3 = ShoppingItem("name", 3, 100f, "url")

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPrice = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPrice).isEqualTo(2 * 10f + 4 * 5.5f + 3 * 100f)
    }
}