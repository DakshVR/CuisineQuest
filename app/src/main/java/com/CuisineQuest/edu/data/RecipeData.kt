package com.CuisineQuest.edu

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity
@TypeConverters(IngredientConverter::class)
class RecipeData(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "instructions") val instructions: String,
    @ColumnInfo(name = "ready_in_minutes") val readyInMinutes: Int,
    @ColumnInfo(name = "vegetarian") val vegetarian: Boolean,
    @ColumnInfo(name = "vegan") val vegan: Boolean,
    @ColumnInfo(name = "gluten_free") val glutenFree: Boolean,
    @ColumnInfo(name = "dairy_free") val dairyFree: Boolean,
    @ColumnInfo(name = "source_name") val sourceName: String,
    @ColumnInfo(name = "ingredients") val ingredients: List<Ingredient>
)


class IngredientConverter {
    @TypeConverter
    fun fromIngredientList(ingredients: List<Ingredient>): String {
        return Gson().toJson(ingredients)
    }

    @TypeConverter
    fun toIngredientList(ingredientsJson: String): List<Ingredient> {
        val type = object : TypeToken<List<Ingredient>>() {}.type
        return Gson().fromJson(ingredientsJson, type)
    }
}

