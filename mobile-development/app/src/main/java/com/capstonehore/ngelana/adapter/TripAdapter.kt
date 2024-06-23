package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.databinding.ItemPlanTripBinding
import com.capstonehore.ngelana.utils.withDateFormat

class TripAdapter :
    ListAdapter<PlanUserItem, TripAdapter.PlanViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = getItem(position)
        holder.bind(plan)
    }

    inner class PlanViewHolder(private val binding: ItemPlanTripBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: PlanUserItem?) {
            plan?.let {
                val imageUrl = itemView.context.getString(R.string.logo_ngelana)

                binding.apply {
                    tripName.text = it.name
                    tripDate.text = it.date?.withDateFormat()
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(tripImage)

                }
            }

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(plan)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PlanUserItem?)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlanUserItem>() {
            override fun areItemsTheSame(oldItem: PlanUserItem, newItem: PlanUserItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PlanUserItem, newItem: PlanUserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}