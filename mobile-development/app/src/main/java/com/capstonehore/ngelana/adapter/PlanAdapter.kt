package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ItemPlanBinding

class PlanAdapter(private val listPlace: ArrayList<Place>) :
    RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlanViewHolder(binding)
    }

    override fun getItemCount(): Int = listPlace.size

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val (name, _, image) = listPlace[position]
        with(holder.binding) {
            placeName.text = name
            Glide.with(holder.itemView.context)
                .load(image)
                .into(placeImage)
        }

        holder.itemView.setOnClickListener {
            @Suppress("DEPRECATION")
            onItemClickCallback.onItemClicked(listPlace[holder.adapterPosition])
        }
    }

    class PlanViewHolder(var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(items: Place)
    }
}