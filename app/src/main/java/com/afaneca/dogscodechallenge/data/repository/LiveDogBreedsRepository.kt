package com.afaneca.dogscodechallenge.data.repository

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.data.remote.DogApi
import com.afaneca.dogscodechallenge.data.remote.entity.mapToDomain
import com.afaneca.dogscodechallenge.domain.model.Breed
import com.afaneca.dogscodechallenge.domain.model.DogItem
import com.afaneca.dogscodechallenge.domain.model.DogItemsOrder
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import javax.inject.Inject

class LiveDogBreedsRepository @Inject constructor(
    private val dogApi: DogApi
) : DogBreedsRepository {
    override suspend fun exploreDogImages(
        page: Int,
        order: DogItemsOrder
    ): Resource<List<DogItem>> {
        return try {
            Resource.Success(
                dogApi.exploreDogImages(page = page, order = order.tag)
                    .map { result -> result.mapToDomain() })
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "")
        }
    }

    override suspend fun searchDogBreeds(query: String, page: Int): Resource<List<Breed>> {
        return try {
            Resource.Success(
                dogApi.searchBreeds(query = query, page = page)
                    .map { result -> result.mapToDomain() })
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "")
        }
    }
}