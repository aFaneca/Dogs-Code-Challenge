package com.afaneca.dogscodechallenge.data.remote

import com.afaneca.dogscodechallenge.common.Constants
import com.afaneca.dogscodechallenge.data.remote.entity.BreedEntity
import com.afaneca.dogscodechallenge.data.remote.entity.DogItemEntity
import retrofit2.Response

interface DogsRemoteDataSource {
    suspend fun exploreDogImages(
        order: String? = "",
        page: Int,
        limit: Int = Constants.DEFAULT_PAGE_SIZE,
        hasBreeds: Int = 1,
    ): Response<List<DogItemEntity>>

    suspend fun searchBreeds(
        query: String,
        page: Int,
        limit: Int = Constants.DEFAULT_PAGE_SIZE,
    ): Response<List<BreedEntity>>
}