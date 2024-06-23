package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.databinding.ItemInterestBinding
import com.capstonehore.ngelana.databinding.ItemProfileBinding

class MyInterestAdapter :
    ListAdapter<PreferenceItem, MyInterestAdapter.InterestViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val binding = ItemProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return InterestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class InterestViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PreferenceItem?) {
            item?.let {
                val imageUrl = itemView.context.getString(R.string.ic_preference)

                binding.apply {
                    tvName.text = it.name
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(ivIconLeft)
                }
            }

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PreferenceItem?)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PreferenceItem>() {
            override fun areItemsTheSame(oldItem: PreferenceItem, newItem: PreferenceItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PreferenceItem, newItem: PreferenceItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}