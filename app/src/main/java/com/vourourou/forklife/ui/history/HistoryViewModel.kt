package com.vourourou.forklife.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vourourou.forklife.data.local.entity.ScanHistoryItem
import com.vourourou.forklife.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val historyItems: StateFlow<List<ScanHistoryItem>> = historyRepository
        .getAllHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Track items pending deletion (barcodes that are hidden but not yet deleted)
    private val _pendingDeleteItems = MutableStateFlow<Set<String>>(emptySet())
    val pendingDeleteItems: StateFlow<Set<String>> = _pendingDeleteItems.asStateFlow()

    // Track deletion jobs for each barcode
    private val deletionJobs = mutableMapOf<String, Job>()

    // Schedule deletion after delay (4 seconds) - persists across navigation
    fun scheduleDeletion(barcode: String) {
        // Cancel any existing deletion job for this barcode
        deletionJobs[barcode]?.cancel()

        // Add to pending deletions
        _pendingDeleteItems.value = _pendingDeleteItems.value + barcode

        // Schedule actual deletion after 4 seconds
        deletionJobs[barcode] = viewModelScope.launch {
            delay(4000) // 4 second delay for undo
            historyRepository.deleteByBarcode(barcode)
            _pendingDeleteItems.value = _pendingDeleteItems.value - barcode
            deletionJobs.remove(barcode)
        }
    }

    // Cancel pending deletion (undo)
    fun cancelDeletion(barcode: String) {
        deletionJobs[barcode]?.cancel()
        deletionJobs.remove(barcode)
        _pendingDeleteItems.value = _pendingDeleteItems.value - barcode
    }

    fun deleteHistoryItem(barcode: String) {
        viewModelScope.launch {
            historyRepository.deleteByBarcode(barcode)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            historyRepository.clearAll()
        }
    }
}
