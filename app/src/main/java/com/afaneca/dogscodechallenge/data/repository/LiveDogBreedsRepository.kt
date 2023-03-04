package com.afaneca.dogscodechallenge.data.repository

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.data.remote.DogApi
import com.afaneca.dogscodechallenge.data.remote.entity.mapToDomain
import com.afaneca.dogscodechallenge.domain.model.DogImage
import com.afaneca.dogscodechallenge.domain.model.DogImagesOrder
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import javax.inject.Inject

class LiveDogBreedsRepository @Inject constructor(
    private val dogApi: DogApi
) : DogBreedsRepository {
    override suspend fun exploreDogImages(
        page: Int,
        order: DogImagesOrder
    ): Resource<List<DogImage>> {
        return try {
            Resource.Success(
                dogApi.exploreDogImages(page = page, order = order.tag)
                    .map { result -> result.mapToDomain() })
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "")
        }
    }
}