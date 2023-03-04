package com.afaneca.dogscodechallenge.ui.search

import com.afaneca.dogscodechallenge.ui.model.DogImageUiModel

data class SearchState(
    val isLoading: Boolean = false,
    val isLoadingFromPagination: Boolean = false,
    val listItems: List<DogImageUiModel>? = null,
    val page: Int = 0,
    val error: String? = null,
    val searchQuery: String? = null,
)