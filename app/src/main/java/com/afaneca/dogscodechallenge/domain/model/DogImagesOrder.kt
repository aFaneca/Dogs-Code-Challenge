package com.afaneca.dogscodechallenge.domain.model

sealed class DogImagesOrder(val tag: String) {
    object Ascending : DogImagesOrder("ASC")
    object Descending : DogImagesOrder("DESC")
}