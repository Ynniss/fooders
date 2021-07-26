package com.esgi.fooders.ui.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.esgi.fooders.R
import com.esgi.fooders.databinding.ActivityMainBinding
import com.esgi.fooders.ui.settings.SettingsActivity
import com.esgi.fooders.utils.DataStoreManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var lastThemeChanged: String

    @Inject
    lateinit var dataStoreManager: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFoodersTheme()

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // https://stackoverflow.com/a/61472200
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.historyFragment,
                    R.id.homeFragment,
                    R.id.profileFragment
                )
            )

        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        hideBottomNavigationBar(navController)
        getFcmToken()
    }

    private fun hideBottomNavigationBar(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.visibility = if (destination.id == R.id.loginFragment) {
                supportActionBar?.hide()
                View.GONE
            } else {
                supportActionBar?.show()
                View.VISIBLE
            }

            if (destination.id == R.id.scanFragment) {
                supportActionBar?.hide()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(IO) {
            Log.d("DATASTORE", dataStoreManager.isUsernameSaved().toString())
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navigation_host)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                lifecycleScope.launch {
                    dataStoreManager.updateUsername("")

                    findNavController(R.id.navigation_host).setGraph(R.navigation.navigation_graph)
                    /*val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .build()
                    withContext(Main) {
                        Navigation.findNavController(binding.root).navigate(
                            R.id.action_loginFragment_to_homeFragment,
                            null,
                            navOptions
                        )
                    }*/
                }
            }
            R.id.settings -> {
                ActivityCompat.startActivity(
                    this,
                    Intent(this, SettingsActivity::class.java),
                    null
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        smartThemeChange()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun smartThemeChange() {
        runBlocking {
            val themeValue = dataStoreManager.readTheme()
            if (themeValue != lastThemeChanged) {
                recreate()
            }
        }
    }

    private fun setFoodersTheme() {
        lifecycleScope.launch(IO) {
            val themeValue = dataStoreManager.readTheme()
            lastThemeChanged = themeValue

            when (themeValue) {
                "Avocado" -> setTheme(R.style.Theme_Fooders_Avocado)
                "Orange" -> setTheme(R.style.Theme_Fooders)
                "Cherry" -> setTheme(R.style.Theme_Fooders_Cherry)
            }
        }
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            //val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }

}