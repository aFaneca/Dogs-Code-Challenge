package com.afaneca.dogscodechallenge

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.model.*
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import kotlinx.coroutines.delay

/**
 * Fake DogBreedsRepository implementation, for unit testing
 */
class FakeDogBreedsRepository(
    private val cachedDogItems: List<DogItem>? = null,
    private val remoteDogItems: List<DogItem>? = null,
    private val cachedBreedItems: List<Breed>? = null,
    private val remoteBreedItems: List<Breed>? = null,
    private val isNetworkAvailable: Boolean = true
) : DogBreedsRepository {
    override suspend fun getDogImagesFromRemote(
        page: Int,
        order: DogItemsOrder
    ): Resource<DogItemsPageWrapper> {
        delay(50L) // Will be skipped automatically by StandardTestDispatcher()
        return if (!isNetworkAvailable)
            Resource.Error("network error")
        else Resource.Success(
            data = DogItemsPageWrapper(
                list = remoteDogItems ?: emptyList(), hasReachedPaginationEnd = false
            )
        )
    }

    override suspend fun getDogImagesFromLocalCache(page: Int, order: String): DogItemsPageWrapper {
        return DogItemsPageWrapper(
            list = cachedDogItems ?: emptyList(),
            hasReachedPaginationEnd = false
        )
    }

    override suspend fun insertDogImagesIntoLocalCache(
        list: List<DogItem>,
        page: Int,
        order: String
    ) {
        // No-op
    }

    override suspend fun getDogBreedsFromRemote(
        query: String,
        page: Int
    ): Resource<BreedItemsPageWrapper> {
        return if (!isNetworkAvailable)
            Resource.Error("network error")
        else Resource.Success(
            data = BreedItemsPageWrapper(
                list = remoteBreedItems ?: emptyList(), hasReachedPaginationEnd = false
            )
        )
    }

    override suspend fun getDogBreedsFromLocalCache(
        query: String,
        page: Int
    ): BreedItemsPageWrapper {
        return BreedItemsPageWrapper(
            list = cachedBreedItems ?: emptyList(),
            hasReachedPaginationEnd = false
        )
    }

    override suspend fun insertDogBreedsIntoLocalCache(
        list: List<Breed>,
        query: String,
        page: Int
    ) {
        // No-op
    }

}