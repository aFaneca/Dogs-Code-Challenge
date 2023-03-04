package com.afaneca.dogscodechallenge.ui.search

import com.afaneca.dogscodechallenge.ui.model.DogItemUiModel

data class SearchState(
    val isLoading: Boolean = false,
    val isLoadingFromPagination: Boolean = false,
    val listItems: List<DogItemUiModel>? = null,
    val page: Int = 0,
    val error: String? = null,
    val searchQuery: String? = null,
)