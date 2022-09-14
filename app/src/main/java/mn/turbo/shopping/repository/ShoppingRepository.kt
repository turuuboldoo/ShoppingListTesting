package mn.turbo.shopping.repository

import androidx.lifecycle.LiveData
import mn.turbo.shopping.common.Resource
import mn.turbo.shopping.data.local.ShoppingItem
import mn.turbo.shopping.data.remote.responses.ImageResponse

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>
    fun observeTotalPrice(): LiveData<Float>
    suspend fun searchForImage(query: String): Resource<ImageResponse>
}