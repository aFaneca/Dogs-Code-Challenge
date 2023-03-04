package com.afaneca.dogscodechallenge.ui.list

import com.afaneca.dogscodechallenge.domain.model.DogImagesOrder
import com.afaneca.dogscodechallenge.ui.model.DogImageUiModel

sealed interface ListLayout {
    object List : ListLayout
    object Grid : ListLayout
    object None : ListLayout
}

sealed interface ListOrder {
    object Ascending : ListOrder
    object Descending : ListOrder
    object None : ListOrder
}

data class ListActionBarState(
    val listLayout: ListLayout = ListLayout.None,
    val listOrder: ListOrder = ListOrder.None
)

data class ListState(
    val isLoading: Boolean = false,
    val listItems: List<DogImageUiModel>? = null,
    val page: Int = 1,
    val error: String? = null,
)

fun ListOrder.mapToDomain() = when (this) {
    is ListOrder.Descending -> DogImagesOrder.Descending
    else -> DogImagesOrder.Ascending
}
