package com.esgi.fooders.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.esgi.fooders.R
import com.esgi.fooders.databinding.ActivityMainBinding
import com.esgi.fooders.ui.profile.viewpager.SuccessEventViewModel
import com.esgi.fooders.ui.settings.SettingsActivity
import com.esgi.fooders.utils.DataStoreManager
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

    private val successEventViewModel: SuccessEventViewModel by viewModels()

    @Inject
    lateinit var dataStoreManager: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setFoodersTheme()

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupEdgeToEdge()

        setSupportActionBar(binding.toolbar)
        applyThemeColors()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.homeFragment
                )
            )

        setupActionBarWithNavController(navController, appBarConfiguration)

        hideActionBarForSpecificScreens(navController)
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.appBarLayout.updatePadding(top = insets.top)

            windowInsets
        }
    }

    private fun hideActionBarForSpecificScreens(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.scanFragment -> supportActionBar?.hide()
                else -> supportActionBar?.show()
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
            R.id.profile -> {
                findNavController(R.id.navigation_host).navigate(R.id.profileFragment)
            }
            R.id.logout -> {
                lifecycleScope.launch {
                    dataStoreManager.updateUsername("")
                    supportFragmentManager.popBackStack()
                    findNavController(R.id.navigation_host).setGraph(R.navigation.navigation_graph)
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
                successEventViewModel.miscUserSuccess("theme")
                recreate()
            }
        }
    }

    private fun applyThemeColors() {
        lifecycleScope.launch(IO) {
            val themeValue = dataStoreManager.readTheme()

            val primaryColor = when (themeValue) {
                "Avocado" -> androidx.core.content.ContextCompat.getColor(this@MainActivity, R.color.md_theme_light_primary_green)
                "Orange" -> androidx.core.content.ContextCompat.getColor(this@MainActivity, R.color.md_theme_light_primary)
                "Cherry" -> androidx.core.content.ContextCompat.getColor(this@MainActivity, R.color.md_theme_light_primary_red)
                else -> androidx.core.content.ContextCompat.getColor(this@MainActivity, R.color.md_theme_light_primary)
            }

            lifecycleScope.launch(kotlinx.coroutines.Dispatchers.Main) {
                binding.toolbar.setBackgroundColor(primaryColor)
                binding.appBarLayout.setBackgroundColor(primaryColor)
                window.statusBarColor = android.graphics.Color.TRANSPARENT
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

}