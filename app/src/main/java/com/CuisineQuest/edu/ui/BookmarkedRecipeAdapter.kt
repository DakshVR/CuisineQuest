package com.CuisineQuest.edu.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.CuisineQuest.edu.R
import com.CuisineQuest.edu.RecipeData
import com.bumptech.glide.Glide

class BookmarkedRecipeAdapter(private val onItemClick: (RecipeData) -> Unit) :
    ListAdapter<RecipeData, BookmarkedRecipeAdapter.BookmarkedRecipeViewHolder>(BookmarkedRecipeComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkedRecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_bookmarked_page, parent, false)
        return BookmarkedRecipeViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: BookmarkedRecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    class BookmarkedRecipeViewHolder(itemView: View, private val onItemClick: (RecipeData) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView? = itemView.findViewById(R.id.tv_recipe_title)
        private val imageView: ImageView? = itemView.findViewById(R.id.iv_recipe_image)


        fun bind(recipe: RecipeData) {
            titleTextView?.text = recipe.title
            if (imageView != null) {
                Glide.with(itemView)
                    .load(recipe.image)
                    .into(imageView)
            }

            itemView.setOnClickListener {
                onItemClick(recipe)
            }
        }
    }

    class BookmarkedRecipeComparator : DiffUtil.ItemCallback<RecipeData>() {
        override fun areItemsTheSame(oldItem: RecipeData, newItem: RecipeData): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: RecipeData, newItem: RecipeData): Boolean {
            return oldItem == newItem
        }
    }
}

