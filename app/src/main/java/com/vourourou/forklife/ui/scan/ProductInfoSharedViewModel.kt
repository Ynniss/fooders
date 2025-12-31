package com.vourourou.forklife.ui.scan

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.data.repository.HistoryRepository
import com.vourourou.forklife.data.repository.OpenFoodFactsRepository
import com.vourourou.forklife.utils.DataStoreManager
import com.vourourou.forklife.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductInfoSharedViewModel @Inject constructor(
    private val repository: OpenFoodFactsRepository,
    private val dataStoreManager: DataStoreManager,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    sealed class ProductInformationsEvent {
        class Success(val result: Resource<Product>) :
            ProductInformationsEvent()

        class Failure(val error: String) : ProductInformationsEvent()
        object Loading : ProductInformationsEvent()
        object Empty : ProductInformationsEvent()
    }


    private var _productInformationsEvent =
        MutableLiveData<ProductInformationsEvent>(ProductInformationsEvent.Empty)
    val productInformationsEvent: LiveData<ProductInformationsEvent> = _productInformationsEvent


    private var _isBeenRequestData =
        MutableLiveData(false)
    val isBeenRequestData: LiveData<Boolean> = _isBeenRequestData


    fun getProductInformations(barcode: String) {
        viewModelScope.launch(IO) {
            withContext(Main) {
                _productInformationsEvent.value = ProductInformationsEvent.Loading
            }
            when (val result = repository.getProduct(barcode)) {
                is Resource.Success -> withContext(Main) {
                    Log.d("RES SUCCESS", "INSIDE IT")
                    _isBeenRequestData.value = true
                    _productInformationsEvent.value = ProductInformationsEvent.Success(result)
                }
                is Resource.Error -> withContext(Main) {
                    Log.e("VM", result.message!!)
                    _isBeenRequestData.value = true
                    _productInformationsEvent.value =
                        ProductInformationsEvent.Failure(result.message!!)
                }
            }
        }
    }

    fun resetBooleanCheck() {
        _isBeenRequestData.value = false
    }

    fun trackScan(activity: Activity, productData: Product? = null) {
        viewModelScope.launch(IO) {
            try {
                // Save to history if product data is available
                productData?.let {
                    historyRepository.insertScanHistory(it)
                }

                // Increment scan count
                dataStoreManager.incrementScanCount()

                val scanCount = dataStoreManager.getScanCount()
                Log.d("ProductInfoVM", "Scan tracked: $scanCount")
            } catch (e: Exception) {
                Log.e("ProductInfoVM", "Error tracking scan", e)
            }
        }
    }
}
