package com.afaneca.dogscodechallenge.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afaneca.dogscodechallenge.databinding.AdapterDogListItemBinding
import com.afaneca.dogscodechallenge.databinding.AdapterDogSearchItemBinding
import com.afaneca.dogscodechallenge.ui.model.DogImageUiModel
import com.afaneca.dogscodechallenge.ui.utils.ImageLoader

sealed class ListViewType(val id: Int) {
    object ExpandedWithImage : ListViewType(1)
    object CollapsedWithInfo : ListViewType(2)
}

class DogListAdapter(
    private val viewType: ListViewType,
    private val onItemClick: (item: DogImageUiModel) -> Unit
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

    override fun getItemViewType(position: Int): Int {
        return viewType.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogListAdapter.ViewHolder {
        return when (viewType) {
            ListViewType.CollapsedWithInfo.id -> {
                val binding =
                    AdapterDogSearchItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return CollapsedWithInfoViewHolder(binding)
            }
            else -> {
                val binding =
                    AdapterDogListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ExpandedWithImageViewHolder(binding)
            }
        }


    }

    override fun onBindViewHolder(holder: DogListAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ExpandedWithImageViewHolder(private val binding: AdapterDogListItemBinding) :
        ViewHolder(binding.root) {
        override fun bind(item: DogImageUiModel) {
            binding.root.setOnClickListener { onItemClick(item) }
            binding.tvName.text = item.breedName
            with(binding.ivImage) {
                ImageLoader.loadImageIntoView(context, item.imgUrl ?: "", this)
            }

        }
    }

    inner class CollapsedWithInfoViewHolder(private val binding: AdapterDogSearchItemBinding) :
        ViewHolder(binding.root) {
        override fun bind(item: DogImageUiModel) {
            binding.root.setOnClickListener { onItemClick(item) }
            binding.tvName.text = item.breedName
            binding.tvGroup.text = item.breedGroup
            binding.tvOrigin.text = item.origin

        }
    }

    abstract inner class ViewHolder(private val rootView: View) :
        RecyclerView.ViewHolder(rootView) {
        abstract fun bind(item: DogImageUiModel)
    }
}