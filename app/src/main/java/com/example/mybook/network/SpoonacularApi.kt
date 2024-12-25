package com.example.mybook.network

import com.example.mybook.network.models.RecipeDetailsResponse
import com.example.mybook.network.models.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.mybook.BuildConfig

interface SpoonacularApi {

    // Метод для получения деталей рецепта
    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String = BuildConfig.SPOONACULAR_API_KEY
    ): RecipeDetailsResponse

    // Метод для поиска рецептов
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String = BuildConfig.SPOONACULAR_API_KEY,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true // Включаем описание рецептов
    ): RecipeResponse
}