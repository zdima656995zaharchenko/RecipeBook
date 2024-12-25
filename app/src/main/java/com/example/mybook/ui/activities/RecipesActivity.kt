package com.example.mybook.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybook.R
import com.example.mybook.network.RetrofitClient
import com.example.mybook.network.SpoonacularApi
import com.example.mybook.network.models.RecipeItem
import com.example.mybook.ui.adapters.RecipeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipesActivity : AppCompatActivity() {

    private lateinit var adapter: RecipeAdapter
    private lateinit var allRecipes: List<RecipeItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes)

        // Получаем категорию из Intent
        val category = intent.getStringExtra("CATEGORY") ?: throw IllegalArgumentException("Category is required")

        // Устанавливаем фон страницы в зависимости от категории
        val rootView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.rootView)
        rootView.setBackgroundResource(getBackgroundResource(category))

        // Настроим кнопку возврата
        val btnBackToMenu: Button = findViewById(R.id.btnBackToMenu)
        btnBackToMenu.setOnClickListener { finish() }

        // Настроим RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewRecipes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecipeAdapter(emptyList(), category)
        recyclerView.adapter = adapter

        // Загружаем рецепты из API
        loadRecipesFromApi(category)

        // Настроим поиск
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterRecipes(newText.orEmpty())
                return true
            }
        })
    }

    // Фильтрация рецептов
    private fun filterRecipes(query: String) {
        val filteredRecipes = allRecipes.filter { recipe ->
            recipe.title.contains(query, ignoreCase = true)
        }
        adapter.updateRecipes(filteredRecipes)
    }

    // Загрузка рецептов из API
    private fun loadRecipesFromApi(category: String) {
        val api = RetrofitClient.instance.create(SpoonacularApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.searchRecipes(category)
                withContext(Dispatchers.Main) {
                    allRecipes = response.results.map {
                        it.copy(shortDescription = getShortDescription(it.title))
                    }
                    adapter.updateRecipes(allRecipes)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RecipesActivity, "Ошибка загрузки рецептов", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Метод для получения короткого описания
    private fun getShortDescription(recipeTitle: String): String {
        return when (recipeTitle) {
            "Creamy Homemade Tomato Soup" -> "A rich and creamy tomato soup made from fresh, ripe tomatoes, perfect for a cozy meal."
            "Italian Kale and Potato Soup" -> "A hearty Italian soup with tender kale and potatoes in a flavorful broth."
            "Perfect Chicken Soup" -> "A comforting soup made with tender chicken and vegetables in a savory broth."
            "Blender Carrot Soup" -> "A smooth and velvety carrot soup made with a quick blend of roasted carrots and seasonings."
            "Fresh Fruit Soup" -> "A light and refreshing soup made with a variety of fresh, juicy fruits."
            "Hearty Chicken & Grilled Corn Soup" -> "A filling and flavorful soup with tender chicken, grilled corn, and a rich broth."
            "Paprika Mushroom Soup" -> "A creamy mushroom soup with a smoky paprika flavor."
            "Black Bean Soup - Cindy Style" -> "A hearty black bean soup with a unique blend of spices and flavors."
            "Broccoli Cheddar Soup" -> "A creamy soup made with fresh broccoli and melted cheddar cheese."
            "The Easiest Beef Pho" -> "A simplified version of the traditional Vietnamese pho, with tender beef and aromatic broth."
            "Greek Side Salad" -> "A classic Greek salad with cucumbers, tomatoes, olives, and feta cheese, drizzled with olive oil and oregano."
            "Cucumber and Cannellini Bean Side Salad" -> "A refreshing salad with crunchy cucumbers and creamy cannellini beans."
            "Pork Schnitzel And Apple Salad" -> "A combination of crispy pork schnitzel and fresh apple slices, creating a savory and sweet salad."
            "Couscous Salad With Roasted Vegetables" -> "A light and flavorful couscous salad with roasted seasonal vegetables."
            "Cous Cous Tofu Salad With Creamy Herb Dressing" -> "A vegan-friendly salad with couscous, tofu, and a creamy herb dressing."
            "Fig, Goat Cheese and Walnut Salad" -> "A sophisticated salad with fresh figs, creamy goat cheese, and crunchy walnuts."
            "Fig, Apple, and Arugula Salad" -> "A fresh, tangy salad with figs, apples, and peppery arugula."
            "Simple Kale Salad" -> "A simple yet tasty salad made with fresh kale and a lemon vinaigrette dressing."
            "Japanese Steak Salad" -> "A flavorful salad with grilled steak, mixed greens, and a savory soy-based dressing."
            "Fennel Salad With Orange" -> "A refreshing salad made with thinly sliced fennel and sweet orange slices."
            "Southwestern Tomato Jam" -> "A sweet and tangy tomato jam with a spicy southwestern twist, perfect for pairing with cheeses or meats."
            "Boozy Bbq Chicken" -> "Tender chicken cooked with a rich and smoky BBQ sauce infused with a hint of alcohol."
            "Farfalle with Peas, Ham and Cream" -> "A creamy pasta dish with farfalle, sweet peas, savory ham, and a luscious cream sauce."
            "Kidney Pie" -> "A hearty savory pie filled with kidney meat, vegetables, and a rich gravy."
            "Singapore Curry" -> "A flavorful curry with tender chicken, spices, and coconut milk, served over rice."
            "Easy Baked Parmesan Chicken" -> "Chicken breasts coated with parmesan and breadcrumbs, baked to golden perfection."
            "Refried Beans with Chorizo" -> "Savory refried beans cooked with spicy chorizo sausage for added flavor."
            "Moroccan Chicken Tagine" -> "A flavorful chicken tagine made with a mix of Moroccan spices, vegetables, and dried fruits."
            "Apple Cheddar Turkey Burgers With Chipotle Yogurt Sauce" -> "A savory turkey burger with a blend of apple and cheddar, topped with spicy chipotle yogurt sauce."
            "Breakfast Biscuits and Gravy" -> "A classic Southern breakfast dish of fluffy biscuits smothered in creamy sausage gravy."
            "Slow Cooker Beef Barley Soup" -> "A comforting and hearty beef and barley soup made in a slow cooker for maximum flavor."
            "Greek Shrimp Orzo" -> "Shrimp cooked with orzo pasta and Mediterranean flavors like feta and olives."
            "Baked Lemon Salmon" -> "Salmon baked to perfection with a zesty lemon and herb seasoning."
            "Thai Street Vendor Salmon Skewers" -> "Flavorful salmon skewers marinated in Thai spices and grilled to perfection."
            "Pan-Roasted Swordfish with Chopped Tomatoes and Lemon Beurre Blanc" -> "Pan-seared swordfish served with a tangy tomato and lemon beurre blanc sauce."
            "Maple and Mustard-Glazed Salmon" -> "Salmon glazed with a sweet and tangy maple and mustard sauce."
            "Crispy-Crowned Guacamole Fish Fillets" -> "Fish fillets topped with crispy breadcrumbs and served with a creamy guacamole."
            "Thai Shrimp" -> "Shrimp cooked with a fragrant Thai-style sauce made from coconut milk and spices."
            "Oysters Diablo" -> "Oysters baked with a spicy Diablo sauce, adding a fiery kick."
            "Thai-Style Fish" -> "Fish cooked in a fragrant Thai sauce made with coconut milk, lemongrass, and lime."
            "Pan-Roasted Beef and Sesame Seeds"  -> "Pan-seared beef served with a crispy sesame seed coating for added texture and flavor."
            else -> "No description available."
        }
    }

    // Получаем ресурс фона в зависимости от категории
    private fun getBackgroundResource(category: String): Int {
        return when (category) {
            "Soups" -> R.drawable.soup_background
            "Salads" -> R.drawable.salads_background
            "Deserts" -> R.drawable.deserts_background
            "Meat" -> R.drawable.meat_background
            "Seafood" -> R.drawable.seafood_background
            else -> throw IllegalArgumentException("Unknown category: $category")
        }
    }
}
