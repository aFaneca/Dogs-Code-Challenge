package com.afaneca.dogscodechallenge.data.remote.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DogImageEntity(
    @SerializedName("breeds")
    val breeds: List<BreedEntity>,
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: String,
    @SerializedName("height")
    val height: String,
)