package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.data.local.entity.Category
import com.capstonehore.ngelana.databinding.ItemCategoryBinding


/**
 * CategoryAdapter is a RecyclerView.Adapter implementation for displaying a list of Category items.
 *
 * @property listPlace The list of Category items to be displayed.
 */
class CategoryAdapter(private val listPlace: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listPlace.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, image) = listPlace[position]
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
        }
    }

    class ListViewHolder(var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(items: Category)
    }
}