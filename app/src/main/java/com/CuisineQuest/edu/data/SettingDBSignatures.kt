package com.CuisineQuest.edu.data

class SettingDBSignatures(private val dao: SettingsDao) {
    suspend fun insertSettings(userPreferences: UserPreferences) = dao.insert(userPreferences)

    suspend fun savedSettings() = dao.getUserPreferencesLiveData()

}