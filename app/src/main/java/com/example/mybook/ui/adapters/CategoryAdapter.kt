package com.example.mybook.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybook.R
import com.example.mybook.model.Category

class CategoryAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // ViewHolder — для привязки представлений элемента списка
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageCategory: ImageView = itemView.findViewById(R.id.imageCategory)
        val textCategoryName: TextView = itemView.findViewById(R.id.textCategoryName)
    }

    // Создание ViewHolder из макета item_category.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.textCategoryName.text = category.name
        holder.imageCategory.setImageResource(category.icon)
    }

    // Количество элементов в списке
    override fun getItemCount(): Int = categories.size
}
