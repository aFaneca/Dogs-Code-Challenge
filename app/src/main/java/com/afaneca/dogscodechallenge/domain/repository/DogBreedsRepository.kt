package com.afaneca.dogscodechallenge.domain.repository

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.model.Breed
import com.afaneca.dogscodechallenge.domain.model.DogItem
import com.afaneca.dogscodechallenge.domain.model.DogItemsOrder

interface DogBreedsRepository {
    suspend fun exploreDogImages(page: Int, order: DogItemsOrder): Resource<List<DogItem>>
    suspend fun searchDogBreeds(query: String, page: Int): Resource<List<Breed>>
}