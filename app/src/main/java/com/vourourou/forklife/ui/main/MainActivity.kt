package com.vourourou.forklife.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.vourourou.forklife.ui.ForkLifeApp
import com.vourourou.forklife.utils.DataStoreManager
import com.vourourou.forklife.utils.PlayGamesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var playGamesManager: PlayGamesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize Play Games SDK
        playGamesManager.initialize()

        // Attempt silent sign-in
        lifecycleScope.launch {
            playGamesManager.signInSilently(this@MainActivity)
        }

        setContent {
            ForkLifeApp(
                dataStoreManager = dataStoreManager
            )
        }
    }
}
