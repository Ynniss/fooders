package com.esgi.fooders.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.fooders.data.repository.ScanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val scanRepository: ScanRepository) : ViewModel() {

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
