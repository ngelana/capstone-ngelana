package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.data.local.entity.Category
import com.capstonehore.ngelana.databinding.ItemCategoryBinding

class CategoryAdapter(private val listPlace: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onButtonClickCallback: OnButtonClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnButtonClickCallback(onButtonClickCallback: OnButtonClickCallback) {
        this.onButtonClickCallback = onButtonClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = listPlace.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val (_, name, description, image) = listPlace[position]
        with(holder.binding) {
            categoryName.text = name
            categoryDescription.text = description
            Glide.with(holder.itemView.context)
                .load(image)
                .into(categoryImage)

            holder.itemView.setOnClickListener {
                @Suppress("DEPRECATION")
                onItemClickCallback.onItemClicked(listPlace[holder.adapterPosition])
            }

            holder.binding.exploreButton.setOnClickListener {
                @Suppress("DEPRECATION")
                onButtonClickCallback.onButtonClicked(listPlace[holder.adapterPosition])
            }
        }
    }

    class CategoryViewHolder(var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(items: Category)
    }

    interface OnButtonClickCallback {
        fun onButtonClicked(item: Category)
    }
}