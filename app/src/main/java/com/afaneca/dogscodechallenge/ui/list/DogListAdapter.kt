package com.afaneca.dogscodechallenge.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afaneca.dogscodechallenge.R
import com.afaneca.dogscodechallenge.databinding.AdapterDogListItemBinding
import com.afaneca.dogscodechallenge.databinding.AdapterDogSearchItemBinding
import com.afaneca.dogscodechallenge.ui.model.DogItemUiModel
import com.afaneca.dogscodechallenge.ui.utils.ImageLoader

sealed class ListViewType(val id: Int) {
    object ExpandedWithImage : ListViewType(1)
    object CompactWithInfo : ListViewType(2)
}

class DogListAdapter(
    private val viewType: ListViewType,
    private val onItemClick: (item: DogItemUiModel) -> Unit
) : ListAdapter<DogItemUiModel, DogListAdapter.ViewHolder>(
    AsyncDifferConfig.Builder(
        DiffCallback()
    ).build()
) {
    private class DiffCallback : DiffUtil.ItemCallback<DogItemUiModel>() {
        override fun areItemsTheSame(oldItem: DogItemUiModel, newItem: DogItemUiModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DogItemUiModel, newItem: DogItemUiModel) =
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
            ListViewType.CompactWithInfo.id -> {
                val binding =
                    AdapterDogSearchItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return CompactWithInfoViewHolder(binding)
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
        override fun bind(item: DogItemUiModel) {
            binding.root.setOnClickListener { onItemClick(item) }
            binding.tvName.text = item.breedName
            with(binding.ivImage) {
                ImageLoader.loadImageIntoView(context, item.imgUrl ?: "", this)
            }
        }
    }

    inner class CompactWithInfoViewHolder(private val binding: AdapterDogSearchItemBinding) :
        ViewHolder(binding.root) {
        override fun bind(item: DogItemUiModel) {
            val localizedUnknown = binding.root.context.getString(R.string.unknown)
            binding.root.setOnClickListener { onItemClick(item) }
            binding.tvName.text = item.breedName.ifBlank { localizedUnknown }
            binding.tvGroup.text = item.breedGroup?.ifBlank { localizedUnknown } ?: localizedUnknown
            binding.tvOrigin.text = item.origin?.ifBlank { localizedUnknown } ?: localizedUnknown
        }
    }

    abstract inner class ViewHolder(rootView: View) :
        RecyclerView.ViewHolder(rootView) {
        abstract fun bind(item: DogItemUiModel)
    }
}