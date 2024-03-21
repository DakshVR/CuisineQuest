package com.CuisineQuest.edu.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.CuisineQuest.edu.R
import com.CuisineQuest.edu.RecipeDBViewModel
import com.CuisineQuest.edu.RecipeData
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch
import java.util.Locale


class BookmarkedDetailedRecipeFragment : Fragment(R.layout.fragment_detailed_recipe) {
    private val args: DetailedRecipeFragmentArgs by navArgs()
    private val recipeViewModel: RecipeDBViewModel by viewModels()


    private var isSaved = true
    private var saveMenuItem: MenuItem? = null
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var recipeDetailView: NestedScrollView
    private var currentRecipe: RecipeData? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recipeId = args.recipeId

        val nameTextView: TextView = view.findViewById(R.id.detail_recipe_name)
        val imageView: ImageView = view.findViewById(R.id.detail_recipe_image)
        val ingredientsView: TextView = view.findViewById(R.id.detail_recipe_ingredients)
        val instructionsView: TextView = view.findViewById(R.id.detail_recipe_instructions)

//        loadingIndicator = view.findViewById(R.id.detail_loading_indicator)
        recipeDetailView = view.findViewById(R.id.nested_scroll_view_id)

        recipeViewModel.getRecipeById(recipeId.toInt()).observe(viewLifecycleOwner) { recipe ->
            currentRecipe = recipe
            Glide.with(imageView.context)
                .load(recipe.image)
                .into(imageView)
            nameTextView.text = recipe.title

            val ingredientList = StringBuilder()
            recipe?.ingredients?.forEach { ingredient ->
                val capitalizedName = ingredient.name.capitalize(Locale.ROOT)
                ingredientList.append("• $capitalizedName\n")
            }
            ingredientsView.text = ingredientList
            val instructions = recipe.instructions.split(".").dropLast(1).joinToString(separator = "\n") { step ->
                "• $step"
            }
            instructionsView.text = instructions

            recipeDetailView.visibility = View.VISIBLE

        }

        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    saveMenuItem?.setIcon(R.drawable.ic_bookmark_add)
                    menuInflater.inflate(R.menu.recipe_detail_save, menu)
                    saveMenuItem = menu.findItem(R.id.save_button)
                    updateBookmarkIcon()
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {

                        R.id.action_share -> {
                            share()
                            true
                        }
                        R.id.save_button -> {
//                            isSaved = !isSaved
                            lifecycleScope.launch {
                                try {
                                    val recipeIdInt = recipeId.toInt()
                                    val recipeForDB = recipeViewModel.getRecipeById(recipeIdInt).value
                                    if (recipeForDB != null) {
                                        recipeViewModel.addRecipeToDB(recipeForDB)
                                        toggleRecipeBookmark(recipeForDB)
                                        updateBookmarkIcon()
                                    }
                                } catch (e: Exception) {
                                    Log.e("Prints", "Error fetching the recipe details: ${e.message}")
                                }
                            }


                            true
                        }
                        else -> false
                    }
                }

            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )


    }

    private fun updateBookmarkIcon() {
        if (!isSaved) {
            saveMenuItem?.setIcon(R.drawable.ic_bookmark_add)
        } else {
            saveMenuItem?.setIcon(R.drawable.ic_bookmark_added)
        }
    }



    private fun toggleRecipeBookmark(recipeData: RecipeData) {
        when (!isSaved) {
            true -> {
                recipeViewModel.addRecipeToDB(recipeData)
            }
            false -> {
                recipeViewModel.deleteRecipeFromDB(recipeData)
            }
        }
    }
    private fun share() {
        currentRecipe?.let { recipe ->
            val ingredientsText = recipe.ingredients.joinToString(separator = "\n") { ingredient ->
                "• ${ingredient.name.capitalize(Locale.ROOT)}"
            }
            val instructionsText = recipe.instructions.split(".").dropLast(1).joinToString(separator = "\n") { step ->
                "• $step"
            }
            val shareText = getString(R.string.share_text_template, recipe.title, ingredientsText, instructionsText)

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, null))
        } ?: run {
            Toast.makeText(context, "Recipe details are not available.", Toast.LENGTH_SHORT).show()
        }
    }
}