package com.esgi.fooders.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.esgi.fooders.R
import com.esgi.fooders.databinding.ActivitySettingsBinding
import com.esgi.fooders.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var lastThemeChanged: String

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFoodersTheme()


        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root, SettingsFragment())
            .commit()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setFoodersTheme() {
        lifecycleScope.launch(Dispatchers.IO) {
            val themeValue = dataStoreManager.readTheme()
            lastThemeChanged = themeValue

            when (themeValue) {
                "Avocado" -> setTheme(R.style.Theme_Fooders_Avocado)
                "Orange" -> setTheme(R.style.Theme_Fooders)
                "Cherry" -> setTheme(R.style.Theme_Fooders_Cherry)
                else -> setTheme(R.style.Theme_Fooders)
            }
        }
    }
}