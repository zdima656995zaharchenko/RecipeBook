package com.example.mybook.ui.activities

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.mybook.R
import com.example.mybook.model.Recipe
import com.example.mybook.model.RecipeDatabase
import com.example.mybook.network.RetrofitClient
import com.example.mybook.network.SpoonacularApi
import com.example.mybook.network.models.RecipeDetailsResponse
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeDetailsActivity : AppCompatActivity() {
    private lateinit var db: RecipeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        db = Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java, "favourite_recipes"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        // Настройка кнопки возврата
        val btnBackToRecipes: Button = findViewById(R.id.btnBackToRecipes)
        btnBackToRecipes.setOnClickListener {
            finish() // Завершить текущую активность и вернуться назад
        }

        // Получение ID рецепта из Intent
        val recipeId = intent.getIntExtra("RECIPE_ID", -1)

        // Настройка UI
        val imageView: ImageView = findViewById(R.id.recipeImage)
        val btnFavourites: ImageView = findViewById(R.id.btnFavourites)
        val nameView: TextView = findViewById(R.id.recipeName)
        val descriptionView: TextView = findViewById(R.id.recipeDescription)

        if (recipeId != -1) {
            // Загрузка данных рецепта из API
            val api = RetrofitClient.instance.create(SpoonacularApi::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.getRecipeDetails(recipeId)
                    withContext(Dispatchers.Main) {
                        displayRecipeDetails(
                            response,
                            imageView,
                            nameView,
                            btnFavourites,
                            descriptionView
                        )

                        btnFavourites.setOnClickListener {
                            if (db.recipeDao().findById(response.id) == null) {
                                db.recipeDao().insertRecipe(
                                    Recipe(
                                        response.id,
                                        response.title,
                                        removeListTags(response.instructions),
                                        response.image
                                    )
                                )
                                btnFavourites.setImageResource(R.drawable.star_filled)
                            } else {
                                db.recipeDao().deleteRecipeById(response.id)
                                btnFavourites.setImageResource(R.drawable.star)
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("myTag", e.message!!)
                        Toast.makeText(
                            this@RecipeDetailsActivity,
                            "Ошибка загрузки данных ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "ID рецепта не найден", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayRecipeDetails(
        recipe: RecipeDetailsResponse,
        imageView: ImageView,
        nameView: TextView,
        btnFavourites: ImageView,
        descriptionView: TextView
    ) {
        // Установка изображения через Picasso
        Picasso.get().load(recipe.image).into(imageView)

        // Установка названия и описания рецепта
        nameView.text = recipe.title
        Log.i("myTag", db.recipeDao().getFavoriteRecipes().toString())
        if (db.recipeDao().findById(recipe.id) == null) {
            btnFavourites.setImageResource(R.drawable.star)
        } else {
            btnFavourites.setImageResource(R.drawable.star_filled)
        }
        descriptionView.text = removeListTags(recipe.instructions) // Инструкции рецепта
    }

    private fun removeListTags(inputText: String?): String {
        // Убрать <li>, </li>, <ol>, </ol>
        if (inputText != null) {
            val regex = """</?(li|ol)[^>]*>""".toRegex()
            return inputText.replace(regex, "")
        } else {
            return ""
        }
    }
}
