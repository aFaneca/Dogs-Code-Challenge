package com.afaneca.dogscodechallenge.data.repository

import com.afaneca.dogscodechallenge.FakeDogsLocalDatasource
import com.afaneca.dogscodechallenge.FakeDogsRemoteDataSource
import com.afaneca.dogscodechallenge.data.local.DogsLocalDataSource
import com.afaneca.dogscodechallenge.data.remote.DogsRemoteDataSource
import com.afaneca.dogscodechallenge.domain.model.Breed
import com.afaneca.dogscodechallenge.domain.model.DogItem
import com.afaneca.dogscodechallenge.domain.model.DogItemsOrder
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class DogBreedsRepositoryTest {
    private lateinit var dogBreedsRepository: DogBreedsRepository

    private val localDataSource: DogsLocalDataSource = mock(FakeDogsLocalDatasource::class.java)
    private val remoteDataSource: DogsRemoteDataSource = mock(FakeDogsRemoteDataSource::class.java)

    @Before
    fun setup() {
        dogBreedsRepository = LiveDogBreedsRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun `getDogImagesFromRemote should call remote data source to get data`() {
        runBlocking {
            val order = DogItemsOrder.Ascending
            dogBreedsRepository.getDogImagesFromRemote(0, order)
            verify(remoteDataSource, times(1)).exploreDogImages(order.tag, 0)
        }
    }

    @Test
    fun `getDogImagesFromLocalCache should call local data source to get data`() {
        runBlocking {
            val page = 0
            val order = DogItemsOrder.Ascending.tag
            `when`(localDataSource.getAllDogResultsInPage(page, order)).thenReturn(emptyList())

            dogBreedsRepository.getDogImagesFromLocalCache(page, order)
            verify(localDataSource, times(1)).getAllDogResultsInPage(page, order)
        }
    }

    @Test
    fun `insertDogImagesIntoLocalCache should call local data source to save data`() {
        runBlocking {
            val list = emptyList<DogItem>()
            val page = 0
            val order = DogItemsOrder.Ascending.tag

            dogBreedsRepository.insertDogImagesIntoLocalCache(list, page, order)
            verify(localDataSource, times(1)).insertAllDogs(anyList())
        }
    }

    @Test
    fun `getDogBreedsFromRemote should call remote data source to get data`() {
        runBlocking {
            val query = "abc"
            val page = 0

            dogBreedsRepository.getDogBreedsFromRemote(query, page)
            verify(remoteDataSource, times(1)).searchBreeds(query, page)
        }
    }

    @Test
    fun `getDogBreedsFromLocalCache should call local data source to get data`() {
        runBlocking {
            val query = "abc"
            val page = 0
            `when`(
                localDataSource.getAllBreedQueryResultsInPage(
                    query,
                    page
                )
            ).thenReturn(emptyList())
            dogBreedsRepository.getDogBreedsFromLocalCache(query, page)
            verify(localDataSource, times(1)).getAllBreedQueryResultsInPage(query, page)
        }
    }

    @Test
    fun `insertDogBreedsIntoLocalCache should call local data source to save data`() {
        runBlocking {
            val list = emptyList<Breed>()
            val page = 0
            val query = "abc"

            dogBreedsRepository.insertDogBreedsIntoLocalCache(list, query, page)
            verify(localDataSource, times(1)).insertAllBreeds(anyList())
        }
    }
}

