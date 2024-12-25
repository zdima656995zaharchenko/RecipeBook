package com.example.mybook.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: Recipe)

    @Query("DELETE FROM favourite_recipes WHERE id = :id")
    fun deleteRecipeById(id: Int)

    @Query("SELECT * FROM favourite_recipes")
    fun getFavoriteRecipes(): List<Recipe>

    @Query("SELECT * FROM favourite_recipes WHERE id LIKE :id " +
            "LIMIT 1")
    fun findById(id: Int): Recipe?
}