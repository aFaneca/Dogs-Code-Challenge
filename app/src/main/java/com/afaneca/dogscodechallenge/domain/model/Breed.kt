package com.afaneca.dogscodechallenge.domain.model

data class Breed(
    val weight: ImperialAndMetricAttributes? = null,
    val height: ImperialAndMetricAttributes? = null,
    val id: String,
    val name: String,
    val bredFor: String? = null,
    val breedGroup: String?,
    val lifeSpan: String? = null,
    val temperament: String?,
    val origin: String?,
)

data class ImperialAndMetricAttributes(
    val imperial: String,
    val metric: String,
)