package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstonehore.ngelana.data.local.entity.Language
import com.capstonehore.ngelana.databinding.ItemPreferencesBinding

class LanguageAdapter(private val listLanguages: ArrayList<Language>) :
    RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemPreferencesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LanguageViewHolder(binding)
    }

    override fun getItemCount(): Int = listLanguages.size

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = listLanguages[position]
        holder.binding.tvPreference.text = language.name

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(language)
        }
    }

    class LanguageViewHolder(var binding: ItemPreferencesBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(language: Language)
    }
}