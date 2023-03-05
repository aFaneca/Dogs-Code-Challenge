package com.afaneca.dogscodechallenge.domain.model

data class BreedItemsPageWrapper(
    val list: List<Breed>,
    val hasReachedPaginationEnd: Boolean
)