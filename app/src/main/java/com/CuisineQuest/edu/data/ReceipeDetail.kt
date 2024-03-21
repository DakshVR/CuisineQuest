package com.CuisineQuest.edu

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RecipeDetail(
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val sourceName: String,
    val extendedIngredients: List<Ingredient>,
    val id: Int,
    val title: String,
    val readyInMinutes: Int,
    val image: String,
    val instructions: String
)

@JsonClass(generateAdapter = true)
data class Ingredient(
    val id: Int,
    val aisle: String,
    val image: String,
    val name: String,
    val amount: Double,
)


