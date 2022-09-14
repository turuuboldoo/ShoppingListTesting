package mn.turbo.shopping.repository

import androidx.lifecycle.LiveData
import mn.turbo.shopping.common.Resource
import mn.turbo.shopping.data.local.ShoppingDao
import mn.turbo.shopping.data.local.ShoppingItem
import mn.turbo.shopping.data.remote.PixabayAPI
import mn.turbo.shopping.data.remote.responses.ImageResponse
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val dao: ShoppingDao,
    private val api: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        dao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        dao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return dao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return dao.observeTotalPrice()
    }

    override suspend fun searchForImage(query: String): Resource<ImageResponse> {
        return try {
            val response = api.searchForImage(query)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error!", null)
            } else {
                Resource.error("Unknown error in else block!", null)
            }
        } catch (e: Exception) {
            Resource.error("searchForImage - ${e.localizedMessage}", null)
        }
    }

}