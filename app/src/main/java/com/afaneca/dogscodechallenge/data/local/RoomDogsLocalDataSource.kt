package com.afaneca.dogscodechallenge.data.local

import com.afaneca.dogscodechallenge.data.local.db.breed.BreedDao
import com.afaneca.dogscodechallenge.data.local.db.breed.BreedEntity
import com.afaneca.dogscodechallenge.data.local.db.dog.DogDao
import com.afaneca.dogscodechallenge.data.local.db.dog.DogEntity
import javax.inject.Inject

class RoomDogsLocalDataSource @Inject constructor(
    private val cachedDogDao: DogDao,
    private val cachedBreedDao: BreedDao,
) : DogsLocalDataSource {
    override suspend fun getAllDogResultsInPage(page: Int, order: String): List<DogEntity> =
        cachedDogDao.getAllResultsInPage(page, order)

    override suspend fun insertAllDogs(dataset: List<DogEntity>): LongArray =
        cachedDogDao.insertAll(dataset)

    override suspend fun getAllBreedQueryResultsInPage(
        searchQuery: String,
        page: Int
    ): List<BreedEntity> = cachedBreedDao.getAllQueryResultsInPage(searchQuery, page)

    override suspend fun insertAllBreeds(dataset: List<BreedEntity>): LongArray =
        cachedBreedDao.insertAll(dataset)
}