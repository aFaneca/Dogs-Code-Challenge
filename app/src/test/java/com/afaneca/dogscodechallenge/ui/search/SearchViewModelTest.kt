package com.afaneca.dogscodechallenge.ui.search

import com.afaneca.dogscodechallenge.FakeDogBreedsRepository
import com.afaneca.dogscodechallenge.common.AppDispatchers
import com.afaneca.dogscodechallenge.domain.model.Breed
import com.afaneca.dogscodechallenge.domain.usecase.SearchDogBreedsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class SearchViewModelTest {
    private val sampleDogBreed = Breed(
        id = "abc",
        name = "name",
        breedGroup = "breedGroup",
        temperament = "temperament",
        origin = "origin"
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = AppDispatchers(
        IO = UnconfinedTestDispatcher()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun destroy() {
        Dispatchers.resetMain()
    }

    //region dog breeds
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoking requestNextPage increments page number`() = runTest {
        val useCase = SearchDogBreedsUseCase(FakeDogBreedsRepository())

        val viewModel = SearchViewModel(testDispatcher, useCase)
        viewModel.requestNextPage()
        Assert.assertEquals(1, viewModel.state.value.page)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onQuerySubmitted is invoked with valid string, search query state is updated`() =
        runTest {
            val useCase = SearchDogBreedsUseCase(
                FakeDogBreedsRepository(
                    cachedBreedItems = listOf(
                        sampleDogBreed
                    )
                )
            )
            val viewModel = SearchViewModel(testDispatcher, useCase)
            val searchQuery = "xyz"
            viewModel.onQuerySubmitted(searchQuery)
            runCurrent()
            Assert.assertTrue(viewModel.state.value.searchQuery == searchQuery)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onQuerySubmitted is invoked with empty string, search query state is not updated`() =
        runTest {
            val useCase = SearchDogBreedsUseCase(
                FakeDogBreedsRepository(
                    cachedBreedItems = listOf(
                        sampleDogBreed
                    )
                )
            )
            val viewModel = SearchViewModel(testDispatcher, useCase)
            val searchQuery = "xyz"
            viewModel.onQuerySubmitted(searchQuery)
            runCurrent()
            Assert.assertTrue(viewModel.state.value.searchQuery == searchQuery)
            val newSearchQuery = ""
            viewModel.onQuerySubmitted(newSearchQuery)
            runCurrent()
            Assert.assertEquals(searchQuery, viewModel.state.value.searchQuery)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when searchDogBreedsUseCase is invoked, first state change is loading to true`() =
        runTest {
            val useCase = SearchDogBreedsUseCase(
                FakeDogBreedsRepository(
                    cachedBreedItems = listOf(
                        sampleDogBreed
                    )
                )
            )
            val viewModel = SearchViewModel(testDispatcher, useCase)
            viewModel.onQuerySubmitted("xyz")
            repeat(3) {
                // search query state is updated, then list state and finally the usecase is invoked
                runCurrent()
            }
            Assert.assertTrue(viewModel.state.value.isLoading)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when searchDogBreedsUseCase is invoked without internet and no cached items, an error is triggered`() =
        runTest {
            val useCase = SearchDogBreedsUseCase(
                FakeDogBreedsRepository(isNetworkAvailable = false)
            )
            val viewModel = SearchViewModel(testDispatcher, useCase)
            viewModel.onQuerySubmitted("xyz")
            advanceUntilIdle()
            Assert.assertTrue(viewModel.state.value.error?.isNotEmpty() == true)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when searchDogBreedsUseCase is invoked without internet and cached items, those are returned`() =
        runTest {
            val useCase = SearchDogBreedsUseCase(
                FakeDogBreedsRepository(
                    cachedBreedItems = listOf(sampleDogBreed, sampleDogBreed),
                    isNetworkAvailable = false
                )
            )
            val viewModel = SearchViewModel(testDispatcher, useCase)
            viewModel.onQuerySubmitted("xyz")
            advanceUntilIdle()
            Assert.assertTrue(viewModel.state.value.listItems?.size == 2)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when searchDogBreedsUseCase is invoked with internet and cached items, the list is returned`() =
        runTest {
            val useCase = SearchDogBreedsUseCase(
                FakeDogBreedsRepository(
                    cachedBreedItems = listOf(sampleDogBreed, sampleDogBreed),
                    remoteBreedItems = listOf(sampleDogBreed, sampleDogBreed),
                    isNetworkAvailable = true
                )
            )
            val viewModel = SearchViewModel(testDispatcher, useCase)
            viewModel.onQuerySubmitted("xyz")
            advanceUntilIdle()
            Assert.assertTrue(viewModel.state.value.listItems?.size == 2)
        }
    //endregion
}