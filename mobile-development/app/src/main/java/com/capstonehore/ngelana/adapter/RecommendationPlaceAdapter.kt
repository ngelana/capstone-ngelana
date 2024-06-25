package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ItemRecommendationPlaceBinding
import com.capstonehore.ngelana.utils.capitalizeEachWord
import com.capstonehore.ngelana.utils.splitAndReplaceCommas

class RecommendationPlaceAdapter
    : ListAdapter<PlaceItem, RecommendationPlaceAdapter.RecommendationPlaceViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onClearButtonClickCallback: OnClearButtonClickCallback
    private lateinit var onAddButtonClickCallback: OnAddButtonClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnClearButtonClickCallback(onClearButtonClickCallback: OnClearButtonClickCallback) {
        this.onClearButtonClickCallback = onClearButtonClickCallback
    }

    fun setOnAddButtonClickCallback(onAddButtonClickCallback: OnAddButtonClickCallback) {
        this.onAddButtonClickCallback = onAddButtonClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationPlaceViewHolder {
        val binding = ItemRecommendationPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RecommendationPlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationPlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RecommendationPlaceViewHolder(private var binding: ItemRecommendationPlaceBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(items: PlaceItem?) {
            items?.let { item ->
                val randomIndex = item.urlPlaceholder?.indices?.random()
                val imageUrl = item.urlPlaceholder?.get(randomIndex ?: 0)

                val typesList = item.types?.splitAndReplaceCommas()

                binding.apply {
                    placeName.text = item.name?.capitalizeEachWord()
                    placeCity.text = itemView.context.getString(R.string.bali_indonesia)
                    placeRating.text = item.rating.toString()
                    placeType.text = typesList?.joinToString(", ")?.capitalizeEachWord()
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(placeImage)
                }
            }

            setupListeners(items)
        }

        private fun setupListeners(item: PlaceItem?) {
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }

            binding.clearButton.setOnClickListener {
                onClearButtonClickCallback.onClearButtonClicked(item)
            }

            binding.addButton.setOnClickListener {
                onAddButtonClickCallback.onAddButtonClicked(item)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(item: PlaceItem?)
    }

    interface OnClearButtonClickCallback {
        fun onClearButtonClicked(item: PlaceItem?)
    }

    interface OnAddButtonClickCallback {
        fun onAddButtonClicked(item: PlaceItem?)
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