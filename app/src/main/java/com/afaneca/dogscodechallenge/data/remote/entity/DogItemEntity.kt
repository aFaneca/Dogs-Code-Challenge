package com.afaneca.dogscodechallenge.data.remote.entity

import androidx.annotation.Keep
import com.afaneca.dogscodechallenge.domain.model.DogItem
import com.google.gson.annotations.SerializedName

@Keep
data class DogItemEntity(
    @SerializedName("breeds")
    val breeds: List<BreedEntity>,
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
)

fun DogItemEntity.mapToDomain() = DogItem(
    breeds = breeds.map { it.mapToDomain() },
    id = id,
    url = url
)