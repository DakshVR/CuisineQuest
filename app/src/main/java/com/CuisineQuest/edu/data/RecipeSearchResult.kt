package com.CuisineQuest.edu

import com.CuisineQuest.edu.data.Recipe
import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class RecipeSearchResult(
    val results: List<Recipe>
)

//@JsonClass(generateAdapter = true)
//data class Recipe(
//    val id: Int,
//    val title: String,
//    val image: String,
//)
