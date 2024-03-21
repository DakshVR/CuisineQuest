package com.CuisineQuest.edu

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipeData: RecipeData)
    @Delete
    suspend fun delete(recipeData: RecipeData)

    @Query("select * from RecipeData")
    fun getAllRecipes(): Flow<List<RecipeData>>

    @Query("select * from recipedata where id = :id limit 1")
    fun getRecipeById(id: Int): Flow<RecipeData>
}