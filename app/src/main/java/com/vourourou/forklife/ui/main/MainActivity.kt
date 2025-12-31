package com.vourourou.forklife.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.vourourou.forklife.ui.ForkLifeApp
import com.vourourou.forklife.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep splash screen visible until DataStore is loaded
        splashScreen.setKeepOnScreenCondition { !isReady }

        enableEdgeToEdge()

        setContent {
            ForkLifeApp(
                dataStoreManager = dataStoreManager,
                onReady = { isReady = true }
            )
        }
    }
}
