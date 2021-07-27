package com.esgi.fooders.ui.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.data.repository.ScanRepository
import com.esgi.fooders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductInfoSharedViewModel @Inject constructor(private val scanRepository: ScanRepository) :
    ViewModel() {

    sealed class ProductInformationsEvent {
        class Success(val result: Resource<ProductInformationsResponse>) :
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
            when (val result = scanRepository.getProductInformations(barcode)) {
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
                else -> Unit
            }
        }
    }

    fun resetBooleanCheck() {
        _isBeenRequestData.value = false
    }
}
