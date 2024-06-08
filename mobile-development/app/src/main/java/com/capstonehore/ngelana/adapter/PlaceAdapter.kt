package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ItemPlaceBinding

class PlaceAdapter(private val listPlace: ArrayList<Place>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

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

    override fun getItemCount(): Int = listPlace.size

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
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
    }

    class PlaceViewHolder(var binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(items: Place)
    }
}