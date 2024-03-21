package com.CuisineQuest.edu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.CuisineQuest.edu.R
import com.CuisineQuest.edu.RecipeDBViewModel


class BookmarkedRecipesFragment : Fragment(R.layout.fragment_bookmarked_recipes) {

    private lateinit var bookmarkedRecipesAdapter: BookmarkedRecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoBookmark: TextView

    private val viewModel: RecipeDBViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_bookmark)
        tvNoBookmark = view.findViewById(R.id.tv_bookmark)

        bookmarkedRecipesAdapter = BookmarkedRecipeAdapter { recipe ->
            val action = BookmarkedRecipesFragmentDirections.actionBookmarkedFragmentToDetailedRecipeFragment(recipe.id.toString())
            findNavController().navigate(action)
        }

        recyclerView.adapter = bookmarkedRecipesAdapter

        viewModel.getAllRecipes().observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                tvNoBookmark.visibility = View.INVISIBLE
                bookmarkedRecipesAdapter.submitList(recipes)
            } else {
                recyclerView.visibility = View.INVISIBLE
                tvNoBookmark.visibility = View.VISIBLE
            }
        }
    }
}