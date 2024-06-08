package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstonehore.ngelana.data.PersonalInformation
import com.capstonehore.ngelana.databinding.ItemPersonalInformationBinding

class PersonalInformationAdapter(private val listProfile: ArrayList<PersonalInformation>) :
    RecyclerView.Adapter<PersonalInformationAdapter.PersonalInformationViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalInformationViewHolder {
        val binding = ItemPersonalInformationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PersonalInformationViewHolder(binding)
    }

    override fun getItemCount(): Int = listProfile.size

    override fun onBindViewHolder(holder: PersonalInformationViewHolder, position: Int) {
        val (title, name) = listProfile[position]
        with(holder.binding) {
            tvTitle.text = title
            tvName.text = name
        }

        holder.itemView.setOnClickListener {
            @Suppress("DEPRECATION")
            onItemClickCallback.onItemClicked(listProfile[holder.adapterPosition])
        }
    }

    class PersonalInformationViewHolder(var binding: ItemPersonalInformationBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(items: PersonalInformation)
    }
}