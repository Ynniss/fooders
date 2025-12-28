package com.vourourou.forklife.ui.profile

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vourourou.forklife.utils.DataStoreManager
import com.vourourou.forklife.utils.PlayGamesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val playGamesManager: PlayGamesManager,
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    val isAuthenticated: StateFlow<Boolean> = playGamesManager.isAuthenticated
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val currentPlayer = playGamesManager.currentPlayer
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val scanCount: StateFlow<Int> = dataStoreManager.scanCountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun signIn(activity: Activity) {
        viewModelScope.launch {
            playGamesManager.signInInteractively(activity)
        }
    }

    fun showLeaderboard(activity: Activity) {
        playGamesManager.showLeaderboard(activity)
    }
}
