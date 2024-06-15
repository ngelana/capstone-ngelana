package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstonehore.ngelana.data.Interest
import com.capstonehore.ngelana.databinding.ItemProfileBinding

class MyInterestAdapter(private val listInterest: ArrayList<Interest>) :
    RecyclerView.Adapter<MyInterestAdapter.MyInterestViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyInterestViewHolder {
        val binding = ItemProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyInterestViewHolder(binding)
    }

    override fun getItemCount(): Int = listInterest.size

    override fun onBindViewHolder(holder: MyInterestViewHolder, position: Int) {
        val (name, icon) = listInterest[position]
        with(holder.binding) {
            tvName.text = name
            ivIconLeft.setImageResource(icon)
        }

        holder.itemView.setOnClickListener {
            @Suppress("DEPRECATION")
            onItemClickCallback.onItemClicked(listInterest[holder.adapterPosition])
        }
    }

    class MyInterestViewHolder(var binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(items: Interest)
    }

}