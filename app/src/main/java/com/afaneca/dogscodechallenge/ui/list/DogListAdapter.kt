package com.afaneca.dogscodechallenge.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afaneca.dogscodechallenge.databinding.AdapterDogListItemBinding
import com.afaneca.dogscodechallenge.ui.model.DogImageUiModel
import com.afaneca.dogscodechallenge.ui.utils.ImageLoader

class DogListAdapter(
    val onItemClick: (item: DogImageUiModel) -> Unit
) : ListAdapter<DogImageUiModel, DogListAdapter.ViewHolder>(
    AsyncDifferConfig.Builder(
        DiffCallback()
    ).build()
) {

    private class DiffCallback : DiffUtil.ItemCallback<DogImageUiModel>() {
        override fun areItemsTheSame(oldItem: DogImageUiModel, newItem: DogImageUiModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DogImageUiModel, newItem: DogImageUiModel) =
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
        fun bind(item: DogImageUiModel) {
            binding.tvName.text = item.breedName
            with(binding.ivImage) {
                ImageLoader.loadImageIntoView(context, item.imgUrl ?: "", this)
            }

        }
    }
}