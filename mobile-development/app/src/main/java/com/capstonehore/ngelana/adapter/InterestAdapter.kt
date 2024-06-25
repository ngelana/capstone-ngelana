package com.capstonehore.ngelana.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.databinding.ItemInterestBinding
import com.capstonehore.ngelana.utils.splitAndReplaceCommas
import java.util.Locale

class InterestAdapter(
    private val selectedItems: SparseBooleanArray
) :
    ListAdapter<PreferenceItem, InterestAdapter.InterestViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val binding = ItemInterestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return InterestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        holder.bind(getItem(position), selectedItems[position])
    }

    inner class InterestViewHolder(private val binding: ItemInterestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(items: PreferenceItem?, isSelected: Boolean) {
            items?.let { item ->
                val imageUrl = ContextCompat.getDrawable(itemView.context, R.drawable.icon_ngelana_black)

                binding.apply {
                    tvInterest.text = item.name?.splitAndReplaceCommas()?.joinToString(", ") { it.replaceFirstChar { it1 ->
                        if (it1.isLowerCase()) it1.titlecase(
                            Locale.getDefault()
                        ) else it1.toString()
                    } } ?: ""
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(icInterest)

                    root.setBackgroundResource(
                        if (isSelected) R.drawable.selected_item_background else R.drawable.rounded_corners_white
                    )
                }
            }

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(items)
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