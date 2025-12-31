package com.vourourou.forklife.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vourourou.forklife.data.local.entity.ScanHistoryItem
import com.vourourou.forklife.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    historyRepository: HistoryRepository
) : ViewModel() {

    val uniqueProductsCount: StateFlow<Int> = historyRepository.getUniqueProductsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalScansCount: StateFlow<Int> = historyRepository.getTotalScansCount()
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val mostScannedProducts: StateFlow<List<ScanHistoryItem>> = historyRepository.getMostScannedProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lastScannedProduct: StateFlow<ScanHistoryItem?> = historyRepository.getLastScannedProduct()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
