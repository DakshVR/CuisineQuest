package com.CuisineQuest.edu.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
)