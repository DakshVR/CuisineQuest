package com.CuisineQuest.edu.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.CuisineQuest.edu.AppDatabase
import com.CuisineQuest.edu.SettingsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsDBViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsDao = SettingsDatabase.getInstance(application).settingsDao()

    fun addSettings(userPreferences: UserPreferences) {
        viewModelScope.launch {
            try {
                settingsDao.insert(userPreferences)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Failed to update user preferences", e)
            }
        }
    }

    val currentUserPreferences: LiveData<UserPreferences?> = settingsDao.getUserPreferencesLiveData()


    suspend fun getCurrentUserPreferences(): UserPreferences? = withContext(Dispatchers.IO) {
        settingsDao.getUserPreferences()
    }

}