package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ItemPopularBinding
import com.capstonehore.ngelana.utils.capitalizeEachWord

class PopularAdapter : ListAdapter<PlaceItem, PopularAdapter.PlaceViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPopularBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaceViewHolder(private var binding: ItemPopularBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceItem?) {
            item?.let {
                val randomIndex = it.urlPlaceholder?.indices?.random()
                val imageUrl = it.urlPlaceholder?.get(randomIndex ?: 0)

                binding.apply {
                    placeName.text = it.name?.capitalizeEachWord()
                    placeRating.text = it.rating.toString()
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(placeImage)
                }
            }

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
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