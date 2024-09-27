package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.databinding.ItemProfileBinding
import com.capstonehore.ngelana.utils.splitAndReplaceCommas
import java.util.Locale

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

        fun bind(items: PreferenceItem?) {
            items?.let { item ->
                val imageUrl = ContextCompat.getDrawable(itemView.context, R.drawable.icon_ngelana_black)

                binding.apply {
                    tvName.text = item.name?.splitAndReplaceCommas()?.joinToString(", ") { it.replaceFirstChar { it1 ->
                        if (it1.isLowerCase()) it1.titlecase(
                            Locale.getDefault()
                        ) else it1.toString()
                    } } ?: ""
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(ivIconLeft)
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