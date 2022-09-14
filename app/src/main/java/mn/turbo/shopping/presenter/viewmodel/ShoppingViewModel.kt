package mn.turbo.shopping.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mn.turbo.shopping.common.Constants
import mn.turbo.shopping.common.Event
import mn.turbo.shopping.common.Resource
import mn.turbo.shopping.data.local.ShoppingItem
import mn.turbo.shopping.data.remote.responses.ImageResponse
import mn.turbo.shopping.repository.ShoppingRepository
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(item: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(item)
    }

    fun insertShoppingItemToDatabase(item: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(item)
    }

    fun insertShoppingItem(
        name: String,
        amountString: String,
        priceString: String
    ) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("field is empty!", null)))
            return
        }

        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("item name too long!", null)))
            return
        }

        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("price too damn high!", null)))
            return
        }

        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("amount is not number", null)))
            return
        }

        val shoppingItem =
            ShoppingItem(name, amount, priceString.toFloat(), _currentImageUrl.value ?: "")
        insertShoppingItemToDatabase(shoppingItem)

        setCurrentImageUrl("")

        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchImages(query: String) {
        if (query.isEmpty()) {
            return
        }

        _images.value = Event(Resource.loading(null))

        viewModelScope.launch {
            val response = repository.searchForImage(query)
            _images.value = Event(Resource.success(response.data))
        }
    }
}
