package com.afaneca.dogscodechallenge

import com.afaneca.dogscodechallenge.data.remote.DogsRemoteDataSource
import com.afaneca.dogscodechallenge.data.remote.entity.BreedEntity
import com.afaneca.dogscodechallenge.data.remote.entity.DogItemEntity
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

open class FakeDogsRemoteDataSource : DogsRemoteDataSource {
    override suspend fun exploreDogImages(
        order: String?,
        page: Int,
        limit: Int,
        hasBreeds: Int
    ): Response<List<DogItemEntity>> {
        return Response.error("".toResponseBody(null), okhttp3.Response.Builder().build())
    }

    override suspend fun searchBreeds(
        query: String,
        page: Int,
        limit: Int
    ): Response<List<BreedEntity>> {
        return Response.error("".toResponseBody(null), okhttp3.Response.Builder().build())
    }

}