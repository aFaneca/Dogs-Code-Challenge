package com.afaneca.dogscodechallenge.data.repository

import com.afaneca.dogscodechallenge.common.Constants.DEFAULT_PAGE_SIZE
import com.afaneca.dogscodechallenge.common.Constants.PAGINATION_COUNT_HEADER_KEY
import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.data.local.db.breed.BreedDao
import com.afaneca.dogscodechallenge.data.local.db.breed.BreedEntity
import com.afaneca.dogscodechallenge.data.local.db.breed.mapToDomain
import com.afaneca.dogscodechallenge.data.local.db.dog.DogDao
import com.afaneca.dogscodechallenge.data.local.db.dog.DogEntity
import com.afaneca.dogscodechallenge.data.local.db.dog.mapToDomain
import com.afaneca.dogscodechallenge.data.remote.DogApi
import com.afaneca.dogscodechallenge.data.remote.entity.mapToDomain
import com.afaneca.dogscodechallenge.domain.model.*
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import java.lang.Integer.parseInt
import javax.inject.Inject

class LiveDogBreedsRepository @Inject constructor(
    private val cachedDogDao: DogDao,
    private val cachedBreedDao: BreedDao,
    private val dogApi: DogApi
) : DogBreedsRepository {

    //region dog images
    override suspend fun getDogImagesFromRemote(
        page: Int,
        order: DogItemsOrder
    ): Resource<DogItemsPageWrapper> {
        return try {
            val response = dogApi.exploreDogImages(page = page, order = order.tag)
            val totalItems =
                response.headers()[PAGINATION_COUNT_HEADER_KEY]?.let { parseInt(it) } ?: 0
            val totalPages = totalItems / DEFAULT_PAGE_SIZE
            val hasReachedPaginationEnd = page == totalPages
            val list = response.body() ?: emptyList()
            Resource.Success(
                DogItemsPageWrapper(
                    list = list
                        .map { result -> result.mapToDomain() },
                    hasReachedPaginationEnd = hasReachedPaginationEnd
                )
            )
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "")
        }
    }

    override suspend fun getDogImagesFromLocalCache(
        page: Int,
        order: String
    ): DogItemsPageWrapper {
        val results = cachedDogDao.getAllResultsInPage(page, order)
        return DogItemsPageWrapper(
            list = results.map { it.mapToDomain() },
            hasReachedPaginationEnd = false
        )
    }

    override suspend fun insertDogImagesIntoLocalCache(
        list: List<DogItem>,
        page: Int,
        order: String
    ) {
        cachedDogDao.insertAll(list.map { DogEntity.mapFromDomain(it, page, order) })
    }
    //endregion

    //region breeds
    override suspend fun getDogBreedsFromRemote(
        query: String,
        page: Int
    ): Resource<BreedItemsPageWrapper> {
        return try {
            val response = dogApi.searchBreeds(query = query, page = page)
            val totalItems =
                response.headers()[PAGINATION_COUNT_HEADER_KEY]?.let { parseInt(it) } ?: 0
            val totalPages = totalItems / DEFAULT_PAGE_SIZE
            val hasReachedPaginationEnd = page == totalPages
            val list = response.body() ?: emptyList()
            Resource.Success(
                BreedItemsPageWrapper(
                    list = list
                        .map { result -> result.mapToDomain() },
                    hasReachedPaginationEnd = hasReachedPaginationEnd
                )
            )
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "")
        }
    }

    override suspend fun getDogBreedsFromLocalCache(
        query: String,
        page: Int
    ): BreedItemsPageWrapper {
        val results = cachedBreedDao.getAllQueryResultsInPage(query, page)
        return BreedItemsPageWrapper(
            list = results.map { it.mapToDomain() },
            hasReachedPaginationEnd = false
        )
    }

    override suspend fun insertDogBreedsIntoLocalCache(
        list: List<Breed>,
        query: String,
        page: Int
    ) {
        cachedBreedDao.insertAll(list.map { BreedEntity.mapFromDomain(it, query, page) })
    }
    //endregion
}