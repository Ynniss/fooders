package com.vourourou.forklife.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vourourou.forklife.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val currentTheme: StateFlow<String> = dataStoreManager.themeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Orange"
        )

    val currentDarkMode: StateFlow<String> = dataStoreManager.darkModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "System"
        )

    val selectedAllergens: StateFlow<Set<String>> = dataStoreManager.selectedAllergensFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    suspend fun updateTheme(theme: String) {
        dataStoreManager.updateTheme(theme)
    }

    suspend fun updateDarkMode(darkMode: String) {
        dataStoreManager.updateDarkMode(darkMode)
    }

    fun toggleAllergen(allergenTag: String, isSelected: Boolean) {
        viewModelScope.launch {
            if (isSelected) {
                dataStoreManager.addAllergen(allergenTag)
            } else {
                dataStoreManager.removeAllergen(allergenTag)
            }
        }
    }
}
