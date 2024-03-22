package com.CuisineQuest.edu

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface SpooncularService {

    @GET("recipes/{id}/information")
    @Headers(
        "X-RapidAPI-Key: 71f09ccfb4msh548f6288ef15615p1ba813jsn1db913c03ca8",
        "X-RapidAPI-Host: spoonacular-recipe-food-nutrition-v1.p.rapidapi.com"

    )
    suspend fun loadRecipeDetails(
        @Path("id") recipeId: String
    ): Response<RecipeDetail>

    @GET("recipes/complexSearch")
    @Headers(
        "X-RapidAPI-Key: 71f09ccfb4msh548f6288ef15615p1ba813jsn1db913c03ca8",
        "X-RapidAPI-Host: spoonacular-recipe-food-nutrition-v1.p.rapidapi.com"
    )
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("cuisine") cuisine: String? = null,
        @Query("excludeCuisine") excludeCuisine: String? = null,
        @Query("diet") diet: String? = null,
        @Query("maxReadyTime") maxReadyTime: Int? = null,
        @Query("sort") sort: String? = null,
    ): Response<RecipeSearchResult>

    companion object {
        private const val BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/"

    fun create(): SpooncularService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(SpooncularService::class.java)
        }
    }
}