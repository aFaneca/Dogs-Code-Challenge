package com.afaneca.dogscodechallenge.data.remote.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BreedEntity(
    @SerializedName("weight")
    val weight: ImperialAndMetricAttributes,
    @SerializedName("height")
    val height: ImperialAndMetricAttributes,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("bred_for")
    val bredFor: String,
    @SerializedName("breed_group")
    val bredGroup: String,
    @SerializedName("life_span")
    val lifeSpan: String,
    @SerializedName("temperament")
    val temperament: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("reference_image_id")
    val referenceImageId: String,
)

@Keep
data class ImperialAndMetricAttributes(
    @SerializedName("imperial")
    val imperial: String,
    @SerializedName("metric")
    val metric: String,
)