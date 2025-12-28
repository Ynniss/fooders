package com.vourourou.forklife.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.vourourou.forklife.R
import com.google.android.gms.games.AuthenticationResult
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayGamesSdk
import com.google.android.gms.games.Player
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayGamesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _currentPlayer = MutableStateFlow<Player?>(null)
    val currentPlayer: StateFlow<Player?> = _currentPlayer.asStateFlow()

    private val leaderboardId: String by lazy {
        context.getString(R.string.leaderboard_total_scans)
    }

    companion object {
        private const val TAG = "PlayGamesManager"
    }

    /**
     * Initialize Play Games SDK
     * Should be called in MainActivity onCreate
     */
    fun initialize() {
        PlayGamesSdk.initialize(context)
    }

    /**
     * Attempt to sign in silently (automatic)
     * Returns true if successful, false otherwise
     */
    suspend fun signInSilently(activity: Activity): Boolean {
        return try {
            val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
            val authResult: AuthenticationResult = gamesSignInClient.isAuthenticated().await()

            if (authResult.isAuthenticated) {
                _isAuthenticated.value = true
                loadPlayerInfo(activity)
                Log.d(TAG, "Silent sign-in successful")
                true
            } else {
                _isAuthenticated.value = false
                Log.d(TAG, "Silent sign-in failed - user not authenticated")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Silent sign-in error", e)
            _isAuthenticated.value = false
            false
        }
    }

    /**
     * Sign in interactively (shows Google account picker)
     * Returns true if successful, false otherwise
     */
    suspend fun signInInteractively(activity: Activity): Boolean {
        return try {
            val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
            val authResult: AuthenticationResult = gamesSignInClient.signIn().await()

            if (authResult.isAuthenticated) {
                _isAuthenticated.value = true
                loadPlayerInfo(activity)
                Log.d(TAG, "Interactive sign-in successful")
                true
            } else {
                _isAuthenticated.value = false
                Log.d(TAG, "Interactive sign-in cancelled or failed")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Interactive sign-in error", e)
            _isAuthenticated.value = false
            false
        }
    }

    /**
     * Load current player information
     */
    private suspend fun loadPlayerInfo(activity: Activity) {
        try {
            val playersClient = PlayGames.getPlayersClient(activity)
            val player = playersClient.currentPlayer.await()
            _currentPlayer.value = player
            Log.d(TAG, "Player info loaded: ${player.displayName}")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading player info", e)
            _currentPlayer.value = null
        }
    }

    /**
     * Submit score to the leaderboard
     */
    suspend fun submitScore(activity: Activity, score: Long) {
        if (!_isAuthenticated.value) {
            Log.w(TAG, "Cannot submit score - user not authenticated")
            return
        }

        try {
            val leaderboardsClient = PlayGames.getLeaderboardsClient(activity)
            leaderboardsClient.submitScore(leaderboardId, score)
            Log.d(TAG, "Score submitted: $score")
        } catch (e: Exception) {
            Log.e(TAG, "Error submitting score", e)
        }
    }

    /**
     * Show leaderboard UI
     */
    fun showLeaderboard(activity: Activity) {
        if (!_isAuthenticated.value) {
            Log.w(TAG, "Cannot show leaderboard - user not authenticated")
            return
        }

        try {
            val leaderboardsClient = PlayGames.getLeaderboardsClient(activity)
            leaderboardsClient.getLeaderboardIntent(leaderboardId)
                .addOnSuccessListener { intent ->
                    activity.startActivityForResult(intent, 9001)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error showing leaderboard", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing leaderboard", e)
        }
    }

    /**
     * Sign out
     * Note: Play Games Services v2 doesn't provide an explicit sign out method.
     * This resets the local authentication state. Users can manage their
     * Google account sign-in through device settings.
     */
    fun signOut() {
        _isAuthenticated.value = false
        _currentPlayer.value = null
        Log.d(TAG, "Local sign out successful - user state cleared")
    }
}
