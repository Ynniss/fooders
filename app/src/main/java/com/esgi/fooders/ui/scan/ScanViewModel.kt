package com.esgi.fooders.ui.scan

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException

class ScanViewModel : ViewModel() {

    private val _progressState = MutableLiveData<Boolean>()
    val progressState: LiveData<Boolean> get() = _progressState

    private val resultsReceivedData = MutableLiveData<Boolean>()
    val resultsReceived: LiveData<Boolean> get() = resultsReceivedData

    private val barcodeData = MutableLiveData<String?>()
    val barcode: LiveData<String?> get() = barcodeData

    init {
        _progressState.value = false
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
