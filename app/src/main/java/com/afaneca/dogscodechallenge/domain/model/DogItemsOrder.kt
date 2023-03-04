package com.afaneca.dogscodechallenge.domain.model

sealed class DogItemsOrder(val tag: String) {
    object Ascending : DogItemsOrder("ASC")
    object Descending : DogItemsOrder("DESC")
}