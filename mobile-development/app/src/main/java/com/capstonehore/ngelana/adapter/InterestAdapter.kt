package com.capstonehore.ngelana.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.databinding.ItemProfileBinding

class InterestAdapter(
    private val selectedItems: SparseBooleanArray
) :
    ListAdapter<PreferenceItem, InterestAdapter.InterestViewHolder>(DIFF_CALLBACK) {

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
        holder.bind(getItem(position), selectedItems[position])
    }

    inner class InterestViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PreferenceItem?, isSelected: Boolean) {
            item?.let {
                binding.apply {
                    tvName.text = it.name
                    ivIconLeft.setImageResource(R.drawable.ic_keyboard_arrow_right)
                    Glide.with(itemView.context)
                        .load(it.urlPlaceholder)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(ivIconRight)

                    root.setBackgroundResource(
                        if (isSelected) R.drawable.selected_item_background else R.drawable.rounded_corners_white
                    )
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