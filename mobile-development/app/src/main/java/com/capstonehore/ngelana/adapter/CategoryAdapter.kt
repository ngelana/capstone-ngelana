package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.local.entity.Category
import com.capstonehore.ngelana.databinding.ItemCategoryBinding

class CategoryAdapter(private val listCategory: ArrayList<Category>) :
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

    override fun getItemCount(): Int = listCategory.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = listCategory[position]
        with(holder.binding) {
            categoryName.text = categoryItem.name
            categoryDescription.text = categoryItem.description
            Glide.with(holder.itemView.context)
                .load(categoryItem.image)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .into(categoryImage)

            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(categoryItem)
            }

            exploreButton.setOnClickListener {
                onButtonClickCallback.onButtonClicked(categoryItem)
            }
        }
    }

    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(item: Category?)
    }

    interface OnButtonClickCallback {
        fun onButtonClicked(item: Category?)
    }
}