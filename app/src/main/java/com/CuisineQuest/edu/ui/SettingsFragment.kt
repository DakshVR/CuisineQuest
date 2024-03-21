package com.CuisineQuest.edu.ui


import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import com.CuisineQuest.edu.R
import com.CuisineQuest.edu.data.SettingsDBViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope

import androidx.preference.ListPreference
import com.CuisineQuest.edu.data.UserPreferences
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {
    private val settingsDBViewModel: SettingsDBViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        setupPreferenceListeners()
    }

    private fun setupPreferenceListeners() {
        findPreference<ListPreference>("cuisine_preference")?.setOnPreferenceChangeListener { _, newValue ->

            Log.d("Prints", "Cuisine $newValue")
            updatePreference { it.copy(cuisine = newValue.toString()) }
            true
        }

        findPreference<ListPreference>("diet_preference")?.setOnPreferenceChangeListener { _, newValue ->
            Log.d("Prints", "Diet: $newValue")
            updatePreference { it.copy(diet = newValue.toString()) }
            true
        }

    }

    private fun updatePreference(updateAction: (UserPreferences) -> UserPreferences) {
        lifecycleScope.launch {
            val currentPreferences = settingsDBViewModel.getCurrentUserPreferences()
            val updatedPreferences = updateAction(currentPreferences ?: UserPreferences())
            settingsDBViewModel.addSettings(updatedPreferences)
        }
    }

}







