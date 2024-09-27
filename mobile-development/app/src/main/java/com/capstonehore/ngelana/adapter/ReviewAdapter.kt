package com.capstonehore.ngelana.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.capstonehore.ngelana.databinding.ItemReviewBinding
import com.capstonehore.ngelana.utils.withDateFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewAdapter(private val apiService: ApiService) :
    ListAdapter<ReviewItem, ReviewAdapter.ReviewViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewItem = getItem(position)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getPlaceById(reviewItem.placeId ?: "")
                val placeItem = response.data

                withContext(Dispatchers.Main) {
                    holder.bind(reviewItem, placeItem)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching place", e)
            }
        }
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reviewItem: ReviewItem, place: PlaceItem?) {
            place?.let {
                val randomIndex = place.urlPlaceholder?.indices?.random()
                val imageUrl = place.urlPlaceholder?.get(randomIndex ?: 0)

                binding.apply {
                    placeName.text = place.name ?: "Unknown Place"

                    reviewRating.text = reviewItem.star.toString()
                    reviewDate.text = reviewItem.date?.withDateFormat() ?: "Unknown Date"
                    reviewDescription.text = reviewItem.review

                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(placeImage)
                }
            }
        }
    }

    companion object {
        private const val TAG = "ReviewAdapter"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewItem>() {
            override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}