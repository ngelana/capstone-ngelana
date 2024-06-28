package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ItemPlanBinding

class PlanAdapter :
    ListAdapter<PlaceItem, PlanAdapter.PlanViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onClearButtonClickCallback: OnClearButtonClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnClearButtonClickCallback(onClearButtonClickCallback: OnClearButtonClickCallback) {
        this.onClearButtonClickCallback = onClearButtonClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlanViewHolder(private val binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: PlaceItem?) {
            items?.let { item ->
                val randomIndex = item.urlPlaceholder?.indices?.random()
                val imageUrl = item.urlPlaceholder?.get(randomIndex ?: 0)

                binding.apply {
                    placeName.text = item.name
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

            binding.clearButton.setOnClickListener {
                onClearButtonClickCallback.onClearButtonClicked(items)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(item: PlaceItem?)
    }

    interface OnClearButtonClickCallback {
        fun onClearButtonClicked(item: PlaceItem?)
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