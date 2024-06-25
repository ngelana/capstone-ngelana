package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ItemPlaceBinding
import com.capstonehore.ngelana.utils.capitalizeEachWord
import com.capstonehore.ngelana.utils.splitAndReplaceCommas

class PlaceAdapter : ListAdapter<PlaceItem, PlaceAdapter.PlaceViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaceViewHolder(private var binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        private var currentLocation: Location? = null

        fun bind(items: PlaceItem?) {
            items?.let { item ->
                val randomIndex = item.urlPlaceholder?.indices?.random()
                val imageUrl = item.urlPlaceholder?.get(randomIndex ?: 0)

                val typesList = item.types?.splitAndReplaceCommas()


//                currentLocation = Location("")
//                currentLocation?.latitude = item.latitude ?: 0.0
//                currentLocation?.longitude = item.longitude ?: 0.0

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

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(items)
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