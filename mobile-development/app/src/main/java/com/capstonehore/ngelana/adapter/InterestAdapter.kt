package com.capstonehore.ngelana.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.Interest
import com.capstonehore.ngelana.databinding.ItemInterestBinding

class InterestAdapter(
    private val listInterest: ArrayList<Interest>,
    private val selectedItems: SparseBooleanArray,
    private val onItemClickCallback: (Int) -> Unit
) :
    RecyclerView.Adapter<InterestAdapter.InterestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val binding = ItemInterestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return InterestViewHolder(binding)
    }

    override fun getItemCount(): Int = listInterest.size

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val (name, icon) = listInterest[position]
        holder.bind(name, icon, selectedItems[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.invoke(position)
        }
    }

    inner class InterestViewHolder(private val binding: ItemInterestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String, icon: Int, isSelected: Boolean) {
            with(binding) {
                tvInterest.text = name
                icInterest.setImageResource(icon)

                root.setBackgroundResource(
                    if (isSelected) R.drawable.selected_item_background else R.drawable.rounded_corners_white
                )
            }
        }
    }

}