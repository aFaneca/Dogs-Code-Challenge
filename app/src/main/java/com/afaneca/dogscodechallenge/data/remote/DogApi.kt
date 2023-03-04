package com.afaneca.dogscodechallenge.data.remote

import com.afaneca.dogscodechallenge.common.Constants.DEFAULT_PAGE_SIZE
import com.afaneca.dogscodechallenge.data.remote.entity.BreedEntity
import com.afaneca.dogscodechallenge.data.remote.entity.DogItemEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApi {

    @GET("images/search")
    suspend fun exploreDogImages(
        @Query("order") order: String? = "",
        @Query("page") page: Int,
        @Query("limit") limit: Int = DEFAULT_PAGE_SIZE,
        @Query("has_breeds") hasBreeds: Int = 1,
    ): List<DogItemEntity>

    @GET("breeds/search")
    suspend fun searchBreeds(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = DEFAULT_PAGE_SIZE,
    ): List<BreedEntity>
}