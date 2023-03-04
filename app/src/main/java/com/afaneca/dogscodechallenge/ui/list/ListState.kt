package com.afaneca.dogscodechallenge.ui.list

import com.afaneca.dogscodechallenge.ui.models.DogImage

sealed interface ListLayout {
    object List : ListLayout
    object Grid : ListLayout
    object None : ListLayout
}

data class ListActionBarState(
    val listLayout: ListLayout = ListLayout.None,
)

data class ListState(
    val isLoading: Boolean = false,
    val listItems: List<DogImage>? = null,
    val page: Int = 1,
)

