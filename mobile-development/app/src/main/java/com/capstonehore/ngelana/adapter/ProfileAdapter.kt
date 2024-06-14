package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstonehore.ngelana.data.local.entity.Profile
import com.capstonehore.ngelana.databinding.ItemProfileBinding

class ProfileAdapter(private val listProfile: ArrayList<Profile>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int = listProfile.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val (_, name, icon) = listProfile[position]
        with(holder.binding) {
            tvName.text = name
            ivIconLeft.setImageResource(icon)
        }

        holder.itemView.setOnClickListener {
            @Suppress("DEPRECATION")
            onItemClickCallback.onItemClicked(listProfile[holder.adapterPosition])
        }
    }

    class ProfileViewHolder(var binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(items: Profile)
    }

}