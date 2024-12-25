package com.example.mybook.network.models

data class RecipeDetailsResponse(
    val id: Int,
    val title: String,
    val image: String,
    val summary: String,
    val instructions: String?
)
