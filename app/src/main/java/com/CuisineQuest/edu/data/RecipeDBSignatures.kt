package com.CuisineQuest.edu

class RecipeDBSignatures (private val dao: RecipeDao) {
    suspend fun insertRecipe(recipeData: RecipeData) = dao.insert(recipeData)

    suspend fun deleteRecipe(recipeData: RecipeData) = dao.delete(recipeData)

    fun getAllRecipes() = dao.getAllRecipes()

    fun getRecipeById(id: Int) = dao.getRecipeById(id)

}