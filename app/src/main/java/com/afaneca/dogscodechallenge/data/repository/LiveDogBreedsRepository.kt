package com.afaneca.dogscodechallenge.data.repository

import com.afaneca.dogscodechallenge.common.Constants.DEFAULT_PAGE_SIZE
import com.afaneca.dogscodechallenge.common.Constants.PAGINATION_COUNT_HEADER_KEY
import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.data.remote.DogApi
import com.afaneca.dogscodechallenge.data.remote.entity.mapToDomain
import com.afaneca.dogscodechallenge.domain.model.BreedItemsPageWrapper
import com.afaneca.dogscodechallenge.domain.model.DogItemsOrder
import com.afaneca.dogscodechallenge.domain.model.DogItemsPageWrapper
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import java.lang.Integer.parseInt
import javax.inject.Inject

class LiveDogBreedsRepository @Inject constructor(
    private val dogApi: DogApi
) : DogBreedsRepository {
    override suspend fun exploreDogImages(
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

    override suspend fun searchDogBreeds(
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
}