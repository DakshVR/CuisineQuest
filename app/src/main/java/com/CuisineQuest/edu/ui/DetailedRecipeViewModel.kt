package com.CuisineQuest.edu.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CuisineQuest.edu.RecipeDetail
import com.CuisineQuest.edu.SpooncularService
import com.CuisineQuest.edu.util.LoadingStatus
import kotlinx.coroutines.launch

//Data is hard coded you need to connect this to database
//class DetailedRecipeViewModel : ViewModel() {
//    private val repository = RecipesRepository()
//
//    private val _searchResults = MutableLiveData<List<Recipe>?>(null)
//    val searchResults: LiveData<List<Recipe>?> = _searchResults
//
//    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
//    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus
//
//    private val _error = MutableLiveData<String?>(null)
//    val error: LiveData<String?> = _error
//
//    fun getRecipeById(recipeId: String): LiveData<Result<Recipe>> {
//        val recipe = MutableLiveData<Result<Recipe>>()
//        viewModelScope.launch {
//            recipe.value = repository.getRecipeById(recipeId)
//        }
//        return recipe
//    }
//}


class DetailedRecipeViewModel : ViewModel() {
//    private val repository = RecipesRepository()

    private val _recipeDetail = MutableLiveData<Result<RecipeDetail>>()
    val recipeDetail: LiveData<Result<RecipeDetail>> = _recipeDetail

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun getRecipeById(recipeId: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            try {
                val result = createSpooncularService().loadRecipeDetails(recipeId)
                if (result.isSuccessful) {
                    val recipeDetail = result.body()
                    Log.d("Prints", "Response: $result")
                    _recipeDetail.value = Result.success(recipeDetail!!)
                    _loadingStatus.value = LoadingStatus.SUCCESS
                } else {
                    _error.value = "Failed to fetch recipe details"
                    _loadingStatus.value = LoadingStatus.ERROR
                }
            } catch (e: Exception) {
                _error.value = e.message
                _loadingStatus.value = LoadingStatus.ERROR
            }
        }
    }

    private fun createSpooncularService(): SpooncularService {
        return SpooncularService.create()
    }
}