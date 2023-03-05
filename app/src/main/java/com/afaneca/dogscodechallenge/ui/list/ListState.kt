package com.afaneca.dogscodechallenge.ui.list

import com.afaneca.dogscodechallenge.domain.model.DogItemsOrder
import com.afaneca.dogscodechallenge.ui.model.DogItemUiModel

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
    val isLoadingFromPagination: Boolean = false,
    val listItems: List<DogItemUiModel>? = null,
    val page: Int = 0,
    val error: String? = null,
    val hasReachedPaginationEnd: Boolean = false,
)

fun ListOrder.mapToDomain() = when (this) {
    is ListOrder.Descending -> DogItemsOrder.Descending
    else -> DogItemsOrder.Ascending
}
