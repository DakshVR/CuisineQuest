package com.CuisineQuest.edu.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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
import com.CuisineQuest.edu.RecipeDetail
import com.CuisineQuest.edu.SpooncularService
import com.CuisineQuest.edu.data.SettingsDBViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.CuisineQuest.edu.util.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch
import java.util.Locale


class DetailedRecipeFragment : Fragment(R.layout.fragment_detailed_recipe) {
    private val args: DetailedRecipeFragmentArgs by navArgs()
    private val viewModel: DetailedRecipeViewModel by viewModels()
    private val recipeViewModel: RecipeDBViewModel by viewModels()
    private val settingsViewModel : SettingsDBViewModel by viewModels()
    private var isSaved = false
    private var saveMenuItem: MenuItem? = null
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var recipeDetailView: NestedScrollView
    private var currentRecipe: RecipeDetail? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = args.recipeId.toString()
        val timerTextView: TextView = view.findViewById(R.id.timer_text_view)

        val nameTextView: TextView = view.findViewById(R.id.detail_recipe_name)
        val imageView: ImageView = view.findViewById(R.id.detail_recipe_image)
        val ingredientsView: TextView = view.findViewById(R.id.detail_recipe_ingredients)
        val instructionsView: TextView = view.findViewById(R.id.detail_recipe_instructions)
        val timerButton: FloatingActionButton = view.findViewById(R.id.fab_timer)

        var recipeForDB: RecipeData
        var recipeID: Int? = 1079930
        var timerDuration = 60000L * 30

        loadingIndicator = view.findViewById(R.id.detail_loading_indicator)
        recipeDetailView = view.findViewById(R.id.nested_scroll_view_id)
        loadingIndicator.visibility = View.VISIBLE


        viewModel.getRecipeById(args.recipeId.toString())

        viewModel.recipeDetail.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                val recipe = result.getOrNull()
                currentRecipe = recipe
                recipeID = recipe?.id!!

                lifecycleScope.launch {
                    try {
                        recipeForDB = getRecipeById(recipeID!!)
                        timerDuration = recipe.readyInMinutes.toLong()
                        timerTextView.hint = "$timerDuration:00"
                    }
                    catch (e: Exception){
                        Log.e("Prints", "Error fetching the recipe details: ${e.message}")
                    }

                }

                Glide.with(imageView.context)
                    .load(recipe?.image)
                    .into(imageView)

                nameTextView.text = recipe?.title

                val ingredientList = StringBuilder()
                recipe?.extendedIngredients?.forEach { ingredient ->
                    val capitalizedName = ingredient.name.capitalize(Locale.ROOT)
                    ingredientList.append("• $capitalizedName\n")
                }
                ingredientsView.text = ingredientList.toString()

                val instructions = recipe?.instructions?.split(".")
                    ?.dropLast(1)
                    ?.joinToString(separator = "\n") { step ->
                        "• $step"
                    }
                instructionsView.text = instructions




            } else {
                val errorMessage = result.exceptionOrNull()?.message
                Log.d("Prints", "Error from view model: $errorMessage")
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.d("Prints", "Error: $errorMessage")
        }


        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                    saveMenuItem?.setIcon(R.drawable.ic_bookmark_add)
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
                            isSaved = !isSaved
                            lifecycleScope.launch {
                                try {
                                    val recipeForDB = recipeID?.let { getRecipeById(it) }
                                    if (recipeForDB != null) {
                                        recipeViewModel.addRecipeToDB(recipeForDB)
                                        toggleRecipeBookmark(recipeForDB)
                                    }
                                } catch (e: Exception) {
                                    Log.e("Prints", "Error fetching the recipe details: ${e.message}")
                                }
                            }

                            updateBookmarkIcon()
                            true
                        }
                        else -> false
                    }
                }

            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        viewModel.loadingStatus.observe(viewLifecycleOwner) { loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    recipeDetailView.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE

                }
                LoadingStatus.ERROR -> {
                    recipeDetailView.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                }
                else -> {
                    recipeDetailView.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                }
            }
        }






        timerButton.setOnClickListener {


            startTimer(timerDuration  * 60000L, timerTextView, timerButton)
        }

    }

    private fun updateBookmarkIcon() {
        if (!isSaved) {
            saveMenuItem?.setIcon(R.drawable.ic_bookmark_added)
        } else {
            saveMenuItem?.setIcon(R.drawable.ic_bookmark_add)
        }
    }



    private fun startTimer(durationInMillis: Long, timerTextView: TextView, floatingActionButton: FloatingActionButton ) {

        object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                floatingActionButton.visibility = View.INVISIBLE
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!"
                floatingActionButton.visibility = View.VISIBLE
            }
        }.start()
    }


    private fun share() {
        currentRecipe?.let { recipe ->
            val ingredientsText = recipe.extendedIngredients.joinToString(separator = "\n") { ingredient ->
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


    private fun createSpooncularService(): SpooncularService {
        return SpooncularService.create()
    }


    private suspend fun fetchRecipeDetails(id: Int): RecipeData {
        val service = createSpooncularService()
        val searchResult = service.loadRecipeDetails(id.toString())

        if (searchResult.isSuccessful) {
            val response = searchResult.body()
            Log.d("MainActivity", "Recipe Details: $response")

            response?.let {
                return RecipeData(
                    id = it.id,
                    title = response.title,
                    dairyFree = response.dairyFree,
                    glutenFree = response.glutenFree,
                    image = response.image,
                    instructions = response.instructions,
                    readyInMinutes = response.readyInMinutes,
                    vegetarian = response.vegetarian,
                    vegan = response.vegan,
                    sourceName = response.sourceName,
                    ingredients = response.extendedIngredients
                )
            }
        }

        throw Exception("Failed to fetch recipe details")
    }
    private suspend fun getRecipeById(id: Int): RecipeData {
        return fetchRecipeDetails(id)
    }


}