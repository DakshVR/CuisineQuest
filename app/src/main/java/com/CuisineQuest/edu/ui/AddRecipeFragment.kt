package com.CuisineQuest.edu.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.CuisineQuest.edu.Ingredient
import com.CuisineQuest.edu.R
import com.CuisineQuest.edu.RecipeDBViewModel
import com.CuisineQuest.edu.RecipeData
import com.google.android.material.textfield.TextInputEditText

class AddRecipeFragment : Fragment(R.layout.fragment_add_recipe) {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var imageUrlEditText: TextInputEditText
    private lateinit var ingredientsEditText: TextInputEditText
    private lateinit var instructionsEditText: TextInputEditText
    private lateinit var saveButton: Button

    private val recipeViewModel: RecipeDBViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = view.findViewById(R.id.name_edittext)
        imageUrlEditText = view.findViewById(R.id.image_url_edittext)
        ingredientsEditText = view.findViewById(R.id.ingredients_edittext)
        instructionsEditText = view.findViewById(R.id.instructions_edittext)
        saveButton = view.findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            saveRecipe()
        }
    }

    private fun saveRecipe() {
        val name = nameEditText.text.toString().trim()
        val imageUrl = imageUrlEditText.text.toString().trim()
        val ingredientsText = ingredientsEditText.text.toString().trim()
        val instructions = instructionsEditText.text.toString().trim()

        if (name.isNotEmpty() && imageUrl.isNotEmpty() && ingredientsText.isNotEmpty() && instructions.isNotEmpty()) {
            val ingredients = ingredientsText.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { Ingredient(0,"unknown", "unknown", it, 0.0) }

            val recipe = RecipeData(
                id = (1..10000).random(),
                title = name,
                image = imageUrl,
                instructions = instructions,
                readyInMinutes = 0,
                vegetarian = false,
                vegan = false,
                glutenFree = false,
                dairyFree = false,
                sourceName = "",
                ingredients = ingredients
            )

            recipeViewModel.addRecipeToDB(recipe)
            Toast.makeText(requireContext(), "Recipe saved", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}