package com.afaneca.dogscodechallenge.domain.model

data class DogImage(
    val breeds: List<Breed>,
    val id: String,
    val url: String,
    val width: String,
    val height: String,
)