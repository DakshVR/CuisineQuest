package com.CuisineQuest.edu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
data class UserPreferences(
    @PrimaryKey(autoGenerate = false) val id: Int = 1,
    @ColumnInfo(name = "cuisine") val cuisine: String? = null,
    @ColumnInfo(name = "excludeCuisine")  val excludeCuisine: String? = null,
    @ColumnInfo(name = "diet") val diet: String? = null,
    @ColumnInfo(name = "maxReadyTime") val maxReadyTime: Int? = null,
    @ColumnInfo(name = "sort") val sort: String? = null
)
