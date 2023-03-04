package com.afaneca.dogscodechallenge.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afaneca.dogscodechallenge.databinding.AdapterDogListItemBinding
import com.afaneca.dogscodechallenge.ui.models.DogImage
import com.afaneca.dogscodechallenge.ui.utils.ImageLoader

class DogListAdapter(
    private val context: Context,
    val onItemClick: (item: DogImage) -> Unit
) : ListAdapter<DogImage, DogListAdapter.ViewHolder>(
    AsyncDifferConfig.Builder<DogImage>(
        DiffCallback()
    ).build()
) {

    private class DiffCallback : DiffUtil.ItemCallback<DogImage>() {
        override fun areItemsTheSame(oldItem: DogImage, newItem: DogImage) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DogImage, newItem: DogImage) =
            oldItem == newItem
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogListAdapter.ViewHolder {
        val binding =
            AdapterDogListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogListAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: AdapterDogListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DogImage) {
            binding.tvName.text = item.breedName
            with(binding.ivImage) {
                ImageLoader.loadImageIntoView(context, item.imgUrl, this)
            }

        }
    }
}