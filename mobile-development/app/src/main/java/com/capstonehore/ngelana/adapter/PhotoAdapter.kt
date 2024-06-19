package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ItemPlacePhotoBinding

class PhotoAdapter(private val listPhotoUrls: List<String>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPlacePhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PhotoViewHolder(binding)
    }

    override fun getItemCount(): Int = listPhotoUrls.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoUrl = listPhotoUrls[position]
        with(holder.binding) {
            Glide.with(holder.itemView.context)
                .load(photoUrl)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .into(placeImage)
        }
    }

    class PhotoViewHolder(var binding: ItemPlacePhotoBinding) : RecyclerView.ViewHolder(binding.root)
}