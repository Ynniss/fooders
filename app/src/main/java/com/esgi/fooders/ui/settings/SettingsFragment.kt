package com.esgi.fooders.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.esgi.fooders.R
import com.esgi.fooders.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val themePreference = findPreference<ListPreference>("chosenTheme")

        themePreference?.setOnPreferenceChangeListener { preference, themeSelectedValue ->

            Log.d("PREF", preference.toString())
            Log.d("PREF VALUE", themeSelectedValue.toString())

            lifecycleScope.launch {
                dataStoreManager.updateTheme(themeSelectedValue.toString())
                requireActivity().finish()
            }
            true
        }
    }


}