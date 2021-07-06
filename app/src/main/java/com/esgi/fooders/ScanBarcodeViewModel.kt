package com.esgi.fooders

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException

class ScanBarcodeViewModel : ViewModel() {

    private val _progressState = MutableLiveData<Boolean>()
    val progressState: LiveData<Boolean> get() = _progressState

    private val resultsReceivedData = MutableLiveData<Boolean>()
    val resultsReceived: LiveData<Boolean> get() = resultsReceivedData

    init {
        _progressState.value = false
    }

    fun searchBarcode(barcode: String) {
        _progressState.value = true
        viewModelScope.launch {
            delay(1000)
            resultsReceivedData.value = true
            _progressState.value = false
        }
    }
}
