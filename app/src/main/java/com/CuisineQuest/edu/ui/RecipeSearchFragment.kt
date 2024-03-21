package com.CuisineQuest.edu.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.CuisineQuest.edu.R
import com.CuisineQuest.edu.data.SettingsDBViewModel
import com.CuisineQuest.edu.data.SettingsDao
import com.CuisineQuest.edu.data.UserPreferences
import com.CuisineQuest.edu.util.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch

class RecipeSearchFragment : Fragment(R.layout.fragment_search),
    SwipeRefreshLayout.OnRefreshListener {
    private val viewModel: RecipeSearchViewModel by viewModels()
    private val settingsDBViewModel: SettingsDBViewModel by viewModels()
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val adapter = RecipeAdapter { recipe ->
        val action = RecipeSearchFragmentDirections.actionSearchFragmentToDetailedRecipeFragment(recipe.id.toString())
        findNavController().navigate(action)
    }


    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var searchQueryEditText: EditText
    private lateinit var searchButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultsListRV = view.findViewById(R.id.searchResultsListRV)
        loadingIndicator = view.findViewById(R.id.loading_indicator)
        searchQueryEditText = view.findViewById(R.id.search_query_edit_text)
        searchButton = view.findViewById(R.id.search_button)
        swipeRefreshLayout = view.findViewById(R.id.srl)




        searchResultsListRV.layoutManager = LinearLayoutManager(context)
        searchResultsListRV.adapter = adapter

        settingsDBViewModel.currentUserPreferences.observe(viewLifecycleOwner) { userPreferences ->
            if (userPreferences != null) {
                Log.d("Prints", "User Preferences: ${userPreferences.cuisine}")
            } else {
                Log.d("Prints", "User Preferences: null")
            }

        }

        searchButton.setOnClickListener {
            performSearchBasedOnPreferences()

        }

        viewModel.searchResults.observe(viewLifecycleOwner) { recipeSearchResult ->
            recipeSearchResult?.let {
                adapter.setRecipes(it.results)
                swipeRefreshLayout.isRefreshing = false
            }

        }

        viewModel.error.observe(viewLifecycleOwner){
            error -> {
                Log.d("e", "Error: $error")
        }

        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) { loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    searchResultsListRV.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                }
                LoadingStatus.ERROR, LoadingStatus.SUCCESS -> {
                    searchResultsListRV.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                }
            }
        }

        swipeRefreshLayout.setOnRefreshListener(this)


    }

    private fun performSearchBasedOnPreferences() {
        val query = searchQueryEditText.text.toString()
        settingsDBViewModel.currentUserPreferences.value?.let { userPreferences ->
            viewModel.searchRecipes(query, userPreferences.cuisine, userPreferences.excludeCuisine, userPreferences.diet, userPreferences.maxReadyTime, userPreferences.sort)
            Log.d("Prints", "At search: $query, ${userPreferences.cuisine}")
        }
        Log.d("Prints", "Searched for: $query")
    }

    override fun onRefresh() {
        val query = searchQueryEditText.text.toString()
        settingsDBViewModel.currentUserPreferences.value?.let { userPreferences ->
            viewModel.searchRecipes(query, userPreferences.cuisine, userPreferences.excludeCuisine, userPreferences.diet, userPreferences.maxReadyTime, userPreferences.sort)
            Log.d("Prints", "At search: $query, ${userPreferences.cuisine}")
        }
        Log.d("Prints", "Searched for: $query")


        val refreshTimeoutMillis: Long = 5000

        Handler(Looper.getMainLooper()).postDelayed({

            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.isRefreshing = false
                Log.d("Refresh", "Refresh timed out and was stopped automatically.")
            }
        }, refreshTimeoutMillis)
    }



}
