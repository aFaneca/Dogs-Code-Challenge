package com.afaneca.dogscodechallenge.data.remote.entity

import androidx.annotation.Keep
import com.afaneca.dogscodechallenge.domain.model.Breed
import com.afaneca.dogscodechallenge.domain.model.ImperialAndMetricAttributes
import com.google.gson.annotations.SerializedName

@Keep
data class BreedEntity(
    @SerializedName("weight")
    val weight: ImperialAndMetricAttributesEntity,
    @SerializedName("height")
    val height: ImperialAndMetricAttributesEntity,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("bred_for")
    val bredFor: String?,
    @SerializedName("breed_group")
    val breedGroup: String?,
    @SerializedName("life_span")
    val lifeSpan: String?,
    @SerializedName("temperament")
    val temperament: String?,
    @SerializedName("origin")
    val origin: String?,
    @SerializedName("reference_image_id")
    val referenceImageId: String?,
)

@Keep
data class ImperialAndMetricAttributesEntity(
    @SerializedName("imperial")
    val imperial: String,
    @SerializedName("metric")
    val metric: String,
)

fun ImperialAndMetricAttributesEntity.mapToDomain() = ImperialAndMetricAttributes(imperial, metric)
fun BreedEntity.mapToDomain() = Breed(
    weight = weight.mapToDomain(),
    height = height.mapToDomain(),
    id = id,
    name= name,
    bredFor = bredFor,
    breedGroup = breedGroup,
    lifeSpan = lifeSpan,
    temperament = temperament,
    origin = origin
)
