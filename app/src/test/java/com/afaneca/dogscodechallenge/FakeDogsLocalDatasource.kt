package com.afaneca.dogscodechallenge

import com.afaneca.dogscodechallenge.data.local.DogsLocalDataSource
import com.afaneca.dogscodechallenge.data.local.db.breed.BreedEntity
import com.afaneca.dogscodechallenge.data.local.db.dog.DogEntity

open class FakeDogsLocalDatasource : DogsLocalDataSource {
    override suspend fun getAllDogResultsInPage(page: Int, order: String): List<DogEntity> {
        return emptyList()
    }

    override suspend fun insertAllDogs(dataset: List<DogEntity>): LongArray {
        return LongArray(1) { 1 }
    }

    override suspend fun getAllBreedQueryResultsInPage(
        searchQuery: String,
        page: Int
    ): List<BreedEntity> {
        return emptyList()
    }

    override suspend fun insertAllBreeds(dataset: List<BreedEntity>): LongArray {
        return LongArray(1) { 1 }
    }

}