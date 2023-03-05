package com.afaneca.dogscodechallenge.domain.model

data class Breed(
    val weight: ImperialAndMetricAttributes,
    val height: ImperialAndMetricAttributes,
    val id: String,
    val name: String,
    val bredFor: String?,
    val breedGroup: String?,
    val lifeSpan: String?,
    val temperament: String?,
    val origin: String?,
)

data class ImperialAndMetricAttributes(
    val imperial: String,
    val metric: String,
)