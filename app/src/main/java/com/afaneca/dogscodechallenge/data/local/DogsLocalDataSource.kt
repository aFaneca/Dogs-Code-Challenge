package com.afaneca.dogscodechallenge.data.local

import com.afaneca.dogscodechallenge.data.local.db.breed.BreedEntity
import com.afaneca.dogscodechallenge.data.local.db.dog.DogEntity

interface DogsLocalDataSource {
    //region dogs
    suspend fun getAllDogResultsInPage(page: Int, order: String): List<DogEntity>

    suspend fun insertAllDogs(dataset: List<DogEntity>): LongArray

    //endregion
    //region breeds
    suspend fun getAllBreedQueryResultsInPage(searchQuery: String, page: Int): List<BreedEntity>

    suspend fun insertAllBreeds(dataset: List<BreedEntity>): LongArray
    //endregion
}