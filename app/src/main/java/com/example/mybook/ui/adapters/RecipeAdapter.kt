package com.example.mybook.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybook.R
import com.example.mybook.ui.activities.RecipeDetailsActivity
import com.example.mybook.network.models.RecipeItem
import com.squareup.picasso.Picasso

class RecipeAdapter(
    private var recipes: List<RecipeItem>,
    private val category: String // Восстановлен параметр category
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeName: TextView = itemView.findViewById(R.id.recipeName)
        val recipeDescription: TextView = itemView.findViewById(R.id.recipeDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.recipeName.text = recipe.title

        // Используем короткое описание, если оно есть
        val descriptionToShow = recipe.shortDescription.takeIf { it.isNotEmpty() }
            ?: recipe.description ?: "Description not available"
        holder.recipeDescription.text = descriptionToShow

        Picasso.get().load(recipe.image).into(holder.recipeImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RecipeDetailsActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<RecipeItem>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}
