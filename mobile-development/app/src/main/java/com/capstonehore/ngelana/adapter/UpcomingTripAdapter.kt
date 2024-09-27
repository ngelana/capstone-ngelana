package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.databinding.ItemUpcomingTripBinding
import com.capstonehore.ngelana.utils.withDateFormat

class UpcomingTripAdapter :
    ListAdapter<PlanUserItem, UpcomingTripAdapter.PlanViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onCompletedButtonClickCallback: OnCompletedButtonClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnCompletedButtonClickCallback(onCompletedButtonClickCallback: OnCompletedButtonClickCallback) {
        this.onCompletedButtonClickCallback = onCompletedButtonClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding =
            ItemUpcomingTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = getItem(position)
        holder.bind(plan)
    }

    inner class PlanViewHolder(private val binding: ItemUpcomingTripBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

            binding.submitButton.setOnClickListener {
                onCompletedButtonClickCallback.onCompletedButtonClicked(plan)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PlanUserItem?)
    }

    interface OnCompletedButtonClickCallback {
        fun onCompletedButtonClicked(data: PlanUserItem?)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlanUserItem>() {
            override fun areItemsTheSame(oldItem: PlanUserItem, newItem: PlanUserItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: PlanUserItem, newItem: PlanUserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}