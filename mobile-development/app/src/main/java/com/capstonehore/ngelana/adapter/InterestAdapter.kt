package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstonehore.ngelana.data.local.entity.Interest
import com.capstonehore.ngelana.databinding.ItemInterestBinding

class InterestAdapter(private val listInterest: ArrayList<Interest>) : RecyclerView.Adapter<InterestAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemInterestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listInterest.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, icon) = listInterest[position]
        with(holder.binding) {
            tvInterest.text = name
            icInterest.setImageResource(icon)
        }

        holder.itemView.setOnClickListener {
            @Suppress("DEPRECATION")
            onItemClickCallback.onItemClicked(listInterest[holder.adapterPosition])
        }
    }

    class ListViewHolder(var binding: ItemInterestBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(items: Interest)
    }
}