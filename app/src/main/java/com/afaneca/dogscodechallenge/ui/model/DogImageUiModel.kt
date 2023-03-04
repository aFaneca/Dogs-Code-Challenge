package com.afaneca.dogscodechallenge.ui.model

import com.afaneca.dogscodechallenge.domain.model.DogImage

data class DogImageUiModel(
    val id: String,
    val breedName: String,
    val breedGroup: String?,
    val origin: String?,
    val temperament: String?,
    val imgUrl: String?,
) {
    companion object {
        fun mapFromDomain(domainModel: DogImage) = DogImageUiModel(
            id = domainModel.id,
            // TODO - make null safe
            breedName = domainModel.breeds.first().name,
            breedGroup = domainModel.breeds.first().breedGroup,
            origin = domainModel.breeds.first().origin,
            temperament = domainModel.breeds.first().temperament,
            imgUrl = domainModel.url
        )
    }
}

