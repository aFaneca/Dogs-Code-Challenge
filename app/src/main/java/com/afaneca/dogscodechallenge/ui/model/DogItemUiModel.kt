package com.afaneca.dogscodechallenge.ui.model

import com.afaneca.dogscodechallenge.domain.model.Breed
import com.afaneca.dogscodechallenge.domain.model.DogItem

data class DogItemUiModel(
    val id: String,
    val breedName: String,
    val breedGroup: String?,
    val origin: String?,
    val temperament: String?,
    val imgUrl: String?,
) {
    companion object {
        fun mapFromDomain(domainModel: DogItem) = DogItemUiModel(
            id = domainModel.id,
            // TODO - make null safe
            breedName = domainModel.breeds.first().name,
            breedGroup = domainModel.breeds.first().breedGroup,
            origin = domainModel.breeds.first().origin,
            temperament = domainModel.breeds.first().temperament,
            imgUrl = domainModel.url
        )

        fun mapFromDomain(domainModel: Breed) = DogItemUiModel(
            id = domainModel.id,
            breedName = domainModel.name,
            breedGroup = domainModel.breedGroup,
            origin = domainModel.origin,
            temperament = domainModel.temperament,
            imgUrl = null
        )
    }
}

