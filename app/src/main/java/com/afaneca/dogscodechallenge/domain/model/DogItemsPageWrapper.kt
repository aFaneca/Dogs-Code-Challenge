package com.afaneca.dogscodechallenge.domain.model

data class DogItemsPageWrapper(
    val list: List<DogItem>,
    val hasReachedPaginationEnd: Boolean
)