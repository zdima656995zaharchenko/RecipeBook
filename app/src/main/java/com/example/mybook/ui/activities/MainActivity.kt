package com.example.mybook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mybook.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Настройка отступов для системных панелей
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Установка обработчиков для кнопок
        findViewById<Button>(R.id.buttonSoups).setOnClickListener {
            navigateToRecipes("Soups")
        }

        findViewById<Button>(R.id.buttonSalads).setOnClickListener {
            navigateToRecipes("Salads")
        }

        findViewById<Button>(R.id.buttonDeserts).setOnClickListener {
            navigateToRecipes("Deserts")
        }

        findViewById<Button>(R.id.buttonMeat).setOnClickListener {
            navigateToRecipes("Meat")
        }

        findViewById<Button>(R.id.buttonSeafood).setOnClickListener {
            navigateToRecipes("Seafood")
        }

        findViewById<ImageView>(R.id.buttonFavourites).setOnClickListener {
            val intent = Intent(this, FavouritesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToRecipes(category: String) {
        val intent = Intent(this, RecipesActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }
}
