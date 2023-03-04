package com.afaneca.dogscodechallenge.domain.repository

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.model.DogImage
import com.afaneca.dogscodechallenge.domain.model.DogImagesOrder

interface DogBreedsRepository {

    suspend fun exploreDogImages(page: Int, order: DogImagesOrder): Resource<List<DogImage>>
}