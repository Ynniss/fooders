package com.esgi.fooders.ui.scan

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val scanRepository: ScanRepository) : ViewModel() {

    sealed class ScanEvent {
        class Success(val result: Resource<ProductInformationsResponse>) : ScanEvent()
        class Failure(val error: String) : ScanEvent()
        object Loading : ScanEvent()
        object Empty : ScanEvent()
    }

    private val _progressState = MutableLiveData<Boolean>()
    val progressState: LiveData<Boolean> get() = _progressState

    private val resultsReceivedData = MutableLiveData<Boolean>()
    val resultsReceived: LiveData<Boolean> get() = resultsReceivedData

    private val barcodeData = MutableLiveData<String?>()
    val barcode: LiveData<String?> get() = barcodeData

    private val _scanEventData = MutableLiveData<ScanEvent>(ScanEvent.Empty)
    val scanEvent: LiveData<ScanEvent> = _scanEventData

    init {
        _progressState.value = false
    }

    fun getProductInformations(barcode: String) {
        viewModelScope.launch(IO) {
            when (val result = scanRepository.getProductInformations(barcode)) {
                is Resource.Success -> withContext(Main) {
                    _scanEventData.value = ScanEvent.Success(result)
                }
                is Resource.Error -> withContext(Main) {
                    _scanEventData.value = ScanEvent.Failure(result.message!!)
                }
                else -> Unit
            }
        }
    }

    fun searchBarcode(barcode: String) {
        _progressState.value = true
        viewModelScope.launch {
            delay(1000)
            resultsReceivedData.value = true
            barcodeData.value = barcode
            _progressState.value = false
        }
    }
}
