package com.afaneca.dogscodechallenge.domain.usecase

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.model.DogItemsOrder
import com.afaneca.dogscodechallenge.domain.model.DogItemsPageWrapper
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExploreDogImagesUseCase @Inject constructor(
    private val dogBreedsRepository: DogBreedsRepository
) {

    suspend operator fun invoke(
        page: Int, order: DogItemsOrder
    ): Flow<Resource<DogItemsPageWrapper>> = flow {
        emit(Resource.Loading())
        when (val response = dogBreedsRepository.getDogImagesFromRemote(page, order)) {
            is Resource.Success -> {
                // Cache results to local data source
                response.data?.list?.let {
                    dogBreedsRepository.insertDogImagesIntoLocalCache(
                        it, page, order.tag
                    )
                }

                // Fetch final list from local data source (our single source of truth)
                val cachedPage = dogBreedsRepository.getDogImagesFromLocalCache(
                    page,
                    order.tag
                )
                emit(Resource.Success(cachedPage))
            }
            is Resource.Error -> {
                // In case of failure (e.g. no internet), return the cached version, if it exists
                val cachedPage = dogBreedsRepository.getDogImagesFromLocalCache(
                    page,
                    order.tag
                )
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