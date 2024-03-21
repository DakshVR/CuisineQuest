package com.CuisineQuest.edu.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.CuisineQuest.edu.R
import com.CuisineQuest.edu.RecipeSearchResult
import com.CuisineQuest.edu.data.Recipe
import com.bumptech.glide.Glide

//class RecipeAdapter(private val onItemClick: (Recipe) -> Unit) :
//    ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(RecipeComparator()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_card, parent, false)
//        return RecipeViewHolder(view, onItemClick)
//    }
//
//    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
//        val recipe = getItem(position)
//        holder.bind(recipe)
//    }
//
//
//    class RecipeViewHolder(private val view: View, val onItemClick: (Recipe) -> Unit) :
//        RecyclerView.ViewHolder(view) {
//        private val nameTextView: TextView = view.findViewById(R.id.recipe_name)
//        private val imageView: ImageView = view.findViewById(R.id.recipe_image)
//
//        fun bind(recipe: Recipe) {
//            nameTextView.text = recipe.name
//            Glide.with(view.context)
//                .load(recipe.image)
//                .fitCenter()
//                .into(imageView)
//
//            view.setOnClickListener {
//                onItemClick(recipe)
//            }
//        }
//    }
//
//
//
//    class RecipeComparator : DiffUtil.ItemCallback<Recipe>() {
//        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
//            return oldItem == newItem
//        }
//    }
//}


class RecipeAdapter(private val onItemClick: (Recipe) -> Unit) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipeList: List<Recipe> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_card, parent, false)
        return RecipeViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipeList.size

    fun setRecipes(recipes: List<Recipe>) {
        recipeList = recipes
        notifyDataSetChanged()
    }



    class RecipeViewHolder(itemView: View, private val onItemClick: (Recipe) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.recipe_name)
        private val imageView: ImageView = itemView.findViewById(R.id.recipe_image)

        fun bind(recipe: Recipe) {
            nameTextView.text = recipe.title
            Glide.with(itemView.context)
                .load(recipe.image)
                .fitCenter()
                .into(imageView)

            itemView.setOnClickListener {
                onItemClick(recipe)
            }
        }
    }
}