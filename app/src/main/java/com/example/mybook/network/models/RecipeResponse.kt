package com.example.mybook.network.models

data class RecipeResponse(
    val results: List<RecipeItem>, // Список рецептов
    val totalResults: Int          // Общее количество рецептов
)

data class RecipeItem(
    val id: Int,
    val title: String,
    val image: String,
    val description: String?,
    val shortDescription: String // Добавлено короткое описание
)
