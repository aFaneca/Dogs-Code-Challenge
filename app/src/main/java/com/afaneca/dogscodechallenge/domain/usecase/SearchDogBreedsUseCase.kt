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
            when (val response = dogBreedsRepository.searchDogBreeds(query, page)) {
                is Resource.Success -> {
                    emit(
                        Resource.Success(
                            BreedItemsPageWrapper(
                                list = response.data?.list ?: emptyList(),
                                hasReachedPaginationEnd = response.data?.hasReachedPaginationEnd == true
                            )
                        )
                    )
                }
                is Resource.Error -> {
                emit(Resource.Error(response.message ?: ""))
            }
            else -> {}
        }
    }
}