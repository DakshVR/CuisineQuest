package com.CuisineQuest.edu.ui

import com.CuisineQuest.edu.util.LoadingStatus
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CuisineQuest.edu.RecipeSearchResult
import com.CuisineQuest.edu.SpooncularService

import kotlinx.coroutines.launch

class RecipeSearchViewModel : ViewModel() {
    private val _searchResults = MutableLiveData<RecipeSearchResult?>(null)
    val searchResults: LiveData<RecipeSearchResult?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun searchRecipes(query: String, cuisine: String?, excludeCuisine: String?, diet: String?, maxReadyTime: Int?, sort: String?) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            try {
                val response = createSpooncularService().searchRecipes(query, cuisine, excludeCuisine, diet, maxReadyTime, sort)
                if (response.isSuccessful) {
                    _searchResults.value = response.body()
                    _loadingStatus.value = LoadingStatus.SUCCESS
                } else {
                    _error.value = "Failed to search recipes"
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