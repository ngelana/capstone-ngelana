package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.data.local.entity.Category
import com.capstonehore.ngelana.databinding.ItemCategoryBinding

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
        val (name, image) = listPlace[position]
        with(holder.binding) {
            placeName.text = name
            Glide.with(holder.itemView.context)
                .load(image)
                .into(placeImage)

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