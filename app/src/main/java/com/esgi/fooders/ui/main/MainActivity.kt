package com.esgi.fooders.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.esgi.fooders.ui.FoodersApp
import com.esgi.fooders.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private var isLoggedIn by mutableStateOf(false)
    private var isInitialized by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Check login state
        lifecycleScope.launch {
            isLoggedIn = dataStoreManager.isUsernameSaved()
            isInitialized = true
        }

        setContent {
            if (isInitialized) {
                FoodersApp(
                    dataStoreManager = dataStoreManager,
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }
}
