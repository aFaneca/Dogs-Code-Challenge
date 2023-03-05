package com.afaneca.dogscodechallenge.domain.repository

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.model.*

interface DogBreedsRepository {

    //region dog images
    suspend fun getDogImagesFromRemote(
        page: Int,
        order: DogItemsOrder
    ): Resource<DogItemsPageWrapper>

    suspend fun getDogImagesFromLocalCache(
        page: Int,
        order: String
    ): DogItemsPageWrapper

    suspend fun insertDogImagesIntoLocalCache(list: List<DogItem>, page: Int, order: String)
    //endregion

    //region breeds
    suspend fun getDogBreedsFromRemote(
        query: String,
        page: Int
    ): Resource<BreedItemsPageWrapper>

    suspend fun getDogBreedsFromLocalCache(
        query: String,
        page: Int
    ): BreedItemsPageWrapper

    suspend fun insertDogBreedsIntoLocalCache(list: List<Breed>, query: String, page: Int)
    //endregion
}