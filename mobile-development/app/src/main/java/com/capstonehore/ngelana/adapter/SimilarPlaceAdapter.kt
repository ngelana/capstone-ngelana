package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ItemSimilarPlaceBinding

class SimilarPlaceAdapter : ListAdapter<PlaceItem, SimilarPlaceAdapter.PlaceViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemSimilarPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaceViewHolder(private var binding: ItemSimilarPlaceBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(items: PlaceItem?) {
            items?.let { item ->
                val randomIndex = item.urlPlaceholder?.indices?.random()
                val imageUrl = item.urlPlaceholder?.get(randomIndex ?: 0)

                binding.apply {
                    placeName.text = item.name
                    placeRating.text = item.rating.toString()
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(placeImage)
                }
            }

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(items)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PlaceItem?)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlaceItem>() {
            override fun areItemsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}