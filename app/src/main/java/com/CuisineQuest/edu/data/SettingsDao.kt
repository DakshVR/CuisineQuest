package com.CuisineQuest.edu.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userPreferences: UserPreferences)


    @Query("SELECT * FROM preferences WHERE id = 1")
    suspend fun getUserPreferences(): UserPreferences?

    @Query("SELECT * FROM preferences WHERE id = 1")
    fun getUserPreferencesLiveData(): LiveData<UserPreferences?>

}
