package com.capstonehore.ngelana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ItemPlanBinding

/**
 * PlanAdapter is a RecyclerView.Adapter implementation for displaying a list of Place items.
 *
 * @property listPlace The list of Place items to be displayed.
 */
class PlanAdapter(private val listPlace: ArrayList<Place>) : RecyclerView.Adapter<PlanAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    /**
     * Sets the callback to be invoked when an item in the list is clicked.
     *
     * @param onItemClickCallback The callback that will run.
     */
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemPlanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )

        return ListViewHolder(binding)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = listPlace.size

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, _, image) = listPlace[position]
        with(holder.binding) {
            placeName.text = name
            Glide.with(holder.itemView.context)
                .load(image)
                .into(placeImage)
        }

        holder.itemView.setOnClickListener {
            @Suppress("DEPRECATION")
            onItemClickCallback.onItemClicked(listPlace[holder.adapterPosition])
        }
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     *
     * @property binding The ItemPlanBinding for the item view.
     */
    class ListViewHolder(var binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Interface definition for a callback to be invoked when an item in this adapter has been clicked.
     */
    interface OnItemClickCallback {
        /**
         * Called when an item in this adapter has been clicked.
         *
         * @param items The item that was clicked.
         */
        fun onItemClicked(items: Place)
    }
}