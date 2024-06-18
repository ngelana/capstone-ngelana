package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ItemRecommendationPlaceBinding

class RecommendationPlaceAdapter(private val listPlace: ArrayList<Place>) :
    RecyclerView.Adapter<RecommendationPlaceAdapter.RecommendationPlaceViewHolder>() {

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

    override fun getItemCount(): Int = listPlace.size

    override fun onBindViewHolder(holder: RecommendationPlaceViewHolder, position: Int) {
        val (name, description, image) = listPlace[position]
        with(holder.binding) {
            placeName.text = name
            placeDescription.text = description
            Glide.with(holder.itemView.context)
                .load(image)
                .into(placeImage)
        }

        holder.itemView.setOnClickListener {
            @Suppress("DEPRECATION")
            onItemClickCallback.onItemClicked(listPlace[holder.adapterPosition])
        }

        holder.binding.clearButton.setOnClickListener {
            @Suppress("DEPRECATION")
            onClearButtonClickCallback.onClearButtonClicked(listPlace[holder.adapterPosition])
        }

        holder.binding.addButton.setOnClickListener {
            onAddButtonClickCallback.onAddButtonClicked(listPlace[position])
        }
    }

    class RecommendationPlaceViewHolder(var binding: ItemRecommendationPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(items: Place)
    }

    interface OnClearButtonClickCallback {
        fun onClearButtonClicked(item: Place)
    }

    interface OnAddButtonClickCallback {
        fun onAddButtonClicked(item: Place)
    }
}