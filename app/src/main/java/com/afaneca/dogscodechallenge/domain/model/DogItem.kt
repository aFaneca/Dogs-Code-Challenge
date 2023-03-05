package com.afaneca.dogscodechallenge.domain.model

data class DogItem(
    val breeds: List<Breed>,
    val id: String,
    val url: String,
)