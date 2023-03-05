package com.afaneca.dogscodechallenge.data.remote

import com.afaneca.dogscodechallenge.data.remote.entity.BreedEntity
import com.afaneca.dogscodechallenge.data.remote.entity.DogItemEntity
import retrofit2.Response
import javax.inject.Inject

class ApiDogsRemoteDataSource @Inject constructor(
    private val dogApi: DogApi,
) : DogsRemoteDataSource {
    override suspend fun exploreDogImages(
        order: String?,
        page: Int,
        limit: Int,
        hasBreeds: Int
    ): Response<List<DogItemEntity>> = dogApi.exploreDogImages(order, page, limit, hasBreeds)

    override suspend fun searchBreeds(
        query: String,
        page: Int,
        limit: Int
    ): Response<List<BreedEntity>> = dogApi.searchBreeds(query, page, limit)
}