package com.example.mybook.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val title: String,
    val instructions: String,
    val image: String
)