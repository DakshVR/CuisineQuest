package com.CuisineQuest.edu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecipeDBViewModel(application: Application): AndroidViewModel(application) {

    private val recipeDBSignatures = RecipeDBSignatures(AppDatabase.getInstance(application).recipeDatadao())

    fun addRecipeToDB(recipeData: RecipeData){
        viewModelScope.launch { recipeDBSignatures.insertRecipe(recipeData) }
    }

    fun deleteRecipeFromDB(recipeData: RecipeData){
        viewModelScope.launch { recipeDBSignatures.deleteRecipe(recipeData) }
    }

    fun getAllRecipes() = recipeDBSignatures.getAllRecipes().asLiveData()


    fun getRecipeById(id: Int) = recipeDBSignatures.getRecipeById(id).asLiveData()

}