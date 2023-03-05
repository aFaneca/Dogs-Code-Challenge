package com.afaneca.dogscodechallenge.domain.repository

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.model.BreedItemsPageWrapper
import com.afaneca.dogscodechallenge.domain.model.DogItemsOrder
import com.afaneca.dogscodechallenge.domain.model.DogItemsPageWrapper

interface DogBreedsRepository {
    suspend fun exploreDogImages(page: Int, order: DogItemsOrder): Resource<DogItemsPageWrapper>
    suspend fun searchDogBreeds(query: String, page: Int): Resource<BreedItemsPageWrapper>
}