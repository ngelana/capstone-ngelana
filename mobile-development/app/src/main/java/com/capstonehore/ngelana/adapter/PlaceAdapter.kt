package com.capstonehore.ngelana.adapter

import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ItemPlaceBinding
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel

class PlaceAdapter(
    private val placeViewModel: PlaceViewModel
) : ListAdapter<PlaceItem, PlaceAdapter.PlaceViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

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

    inner class PlaceViewHolder(private var binding: ItemPlaceBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private var currentLocation: Location? = null

        fun bind(item: PlaceItem?) {
            item?.let {
                val randomIndex = item.urlPlaceholder?.indices?.random()
                val imageUrl = item.urlPlaceholder?.get(randomIndex ?: 0)

                currentLocation = Location("")
                currentLocation?.latitude = item.latitude ?: 0.0
                currentLocation?.longitude = item.longitude ?: 0.0

                binding.apply {
                    placeName.text = item.name
                    placeRating.text = item.rating.toString()
                    placeType.text = item.types?.joinToString(", ") { it }
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(placeImage)
                }
            }

            setupLocation()
            bindCircleView()
            setupListeners(item)
        }

        private fun setupLocation() {
            currentLocation?.let { location ->
                placeViewModel.getLocationDetails(itemView.context, location)

                placeViewModel.locationResult.observe(itemView.context as LifecycleOwner) { result ->
                    when (result) {
                        is Result.Success -> {
                            val response = result.data
                            binding.placeCity.text = response.locality ?: itemView.context.getString(R.string.unknown)
                        }
                        is Result.Error -> {
                            binding.placeCity.text = itemView.context.getString(R.string.unknown)
                            Log.e(TAG, "Failed to get location details: ${result.error}")
                        }
                        is Result.Loading -> {}
                    }
                }
            }
        }

        private fun bindCircleView() {
            val circleView = View(itemView.context)
            circleView.id = View.generateViewId()
            circleView.background = ContextCompat.getDrawable(itemView.context, R.drawable.circle)

            binding.constraintLayout.addView(circleView)

            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.constraintLayout)

            constraintSet.connect(
                circleView.id, ConstraintSet.START,
                R.id.placeType, ConstraintSet.END, 8
            )
            constraintSet.connect(
                circleView.id, ConstraintSet.TOP,
                R.id.placeType, ConstraintSet.TOP
            )
            constraintSet.connect(
                circleView.id, ConstraintSet.BOTTOM,
                R.id.placeType, ConstraintSet.BOTTOM
            )

            constraintSet.applyTo(binding.constraintLayout)
        }

        private fun setupListeners(item: PlaceItem?) {
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
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
        private const val TAG = "PlaceAdapter"
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