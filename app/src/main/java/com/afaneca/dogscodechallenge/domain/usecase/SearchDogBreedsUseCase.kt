package com.afaneca.dogscodechallenge.domain.usecase

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.model.BreedItemsPageWrapper
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchDogBreedsUseCase @Inject constructor(
    private val dogBreedsRepository: DogBreedsRepository
) {

    suspend operator fun invoke(query: String, page: Int): Flow<Resource<BreedItemsPageWrapper>> =
        flow {
            emit(Resource.Loading())
            when (val response = dogBreedsRepository.getDogBreedsFromRemote(query, page)) {
                is Resource.Success -> {
                    // Cache results to local data source
                    response.data?.list?.let {
                        dogBreedsRepository.insertDogBreedsIntoLocalCache(it, query, page)
                    }

                    // Fetch final list from local data source (our single source of truth)
                    val cachedPage = dogBreedsRepository.getDogBreedsFromLocalCache(query, page)
                    emit(Resource.Success(cachedPage))
                }
                is Resource.Error -> {
                    // In case of failure (e.g. no internet), return the cached version, if it exists
                    val cachedPage = dogBreedsRepository.getDogBreedsFromLocalCache(query, page)
                    if (cachedPage.list.isEmpty()) {
                        // If cached version doesn't exist, emit error
                        emit(Resource.Error(response.message ?: ""))
                    } else {
                        emit(Resource.Success(cachedPage))
                    }

                }
                else -> {}
            }
        }
}