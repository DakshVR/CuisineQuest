package com.CuisineQuest.edu

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.CuisineQuest.edu.data.SettingsDao
import com.CuisineQuest.edu.data.UserPreferences

@Database(entities = [UserPreferences::class], version = 1, exportSchema = false)
abstract class SettingsDatabase: RoomDatabase() {

    abstract fun settingsDao() : SettingsDao

    companion object {
        @Volatile
        private var instance: SettingsDatabase? = null
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            SettingsDatabase::class.java,
            "settings_db"
        ).build()

        fun getInstance(context: Context): SettingsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }

}