package com.afaneca.dogscodechallenge.domain.usecase

import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.model.DogImage
import com.afaneca.dogscodechallenge.domain.model.DogImagesOrder
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExploreDogImagesUseCase @Inject constructor(
    private val dogBreedsRepository: DogBreedsRepository
) {

    suspend operator fun invoke(order: DogImagesOrder): Flow<Resource<List<DogImage>>> =
        flow {
            emit(Resource.Loading())
            when (val response = dogBreedsRepository.exploreDogImages(order)) {
                is Resource.Success -> {
                    emit(Resource.Success(response.data ?: emptyList()))
                }
                is Resource.Error -> {
                    emit(Resource.Error(response.message ?: ""))
                }
                else -> {}
            }
        }
}