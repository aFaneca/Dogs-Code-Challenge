package com.afaneca.dogscodechallenge.ui.list

import com.afaneca.dogscodechallenge.FakeDogBreedsRepository
import com.afaneca.dogscodechallenge.common.AppDispatchers
import com.afaneca.dogscodechallenge.domain.model.*
import com.afaneca.dogscodechallenge.domain.usecase.ExploreDogImagesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ListViewModelTest {
    private val sampleDogBreed = Breed(
        id = "abc",
        name = "name",
        breedGroup = "breedGroup",
        temperament = "temperament",
        origin = "origin"
    )
    private val sampleDogItem = DogItem(breeds = listOf(sampleDogBreed), "xyz", "url")

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

    //region dog images
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoking requestNextPage increments page number`() = runTest {
        val useCase = ExploreDogImagesUseCase(FakeDogBreedsRepository())

        val viewModel = ListViewModel(testDispatcher, useCase)
        viewModel.requestNextPage()
        Assert.assertEquals(1, viewModel.state.value.page)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when exploreDogImagesUseCase is invoked, first state change is loading to true`() =
        runTest(StandardTestDispatcher()) {
            val useCase = ExploreDogImagesUseCase(
                FakeDogBreedsRepository(
                    cachedDogItems = listOf(
                        sampleDogItem
                    )
                )
            )
            val viewModel = ListViewModel(testDispatcher, useCase)
            runCurrent()
            Assert.assertTrue(viewModel.state.value.isLoading)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when exploreDogImagesUseCase is invoked without internet and no cached items, an error is triggered`() =
        runTest {
            val useCase = ExploreDogImagesUseCase(
                FakeDogBreedsRepository(isNetworkAvailable = false)
            )
            val viewModel = ListViewModel(testDispatcher, useCase)
            advanceUntilIdle()
            Assert.assertTrue(viewModel.state.value.error?.isNotEmpty() == true)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when exploreDogImagesUseCase is invoked without internet and cached items, those are returned`() =
        runTest {
            val useCase = ExploreDogImagesUseCase(
                FakeDogBreedsRepository(
                    cachedDogItems = listOf(sampleDogItem, sampleDogItem),
                    isNetworkAvailable = false
                )
            )
            val viewModel = ListViewModel(testDispatcher, useCase)
            advanceUntilIdle()
            Assert.assertTrue(viewModel.state.value.listItems?.size == 2)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when exploreDogImagesUseCase is invoked with internet and cached items, the list is returned`() =
        runTest {
            val useCase = ExploreDogImagesUseCase(
                FakeDogBreedsRepository(
                    cachedDogItems = listOf(sampleDogItem, sampleDogItem),
                    remoteDogItems = listOf(sampleDogItem, sampleDogItem),
                    isNetworkAvailable = true
                )
            )
            val viewModel = ListViewModel(testDispatcher, useCase)
            advanceUntilIdle()
            Assert.assertTrue(viewModel.state.value.listItems?.size == 2)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when toggleListOrder is invoked, list state is reset`() =
        runTest {
            val useCase = ExploreDogImagesUseCase(
                FakeDogBreedsRepository(
                    cachedDogItems = listOf(sampleDogItem, sampleDogItem),
                    remoteDogItems = listOf(sampleDogItem, sampleDogItem),
                    isNetworkAvailable = true
                )
            )
            val viewModel = ListViewModel(testDispatcher, useCase)
            advanceUntilIdle()
            Assert.assertTrue(viewModel.state.value.listItems?.size == 2)

            viewModel.toggleListOrder()
            runCurrent()
            Assert.assertTrue(viewModel.state.value.page == 0)
            Assert.assertTrue(viewModel.state.value.listItems == null)
            Assert.assertTrue(!viewModel.state.value.hasReachedPaginationEnd)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when toggleListOrder is invoked, order state is toggled`() =
        runTest {
            val useCase = ExploreDogImagesUseCase(
                FakeDogBreedsRepository(
                    cachedDogItems = listOf(sampleDogItem, sampleDogItem),
                    remoteDogItems = listOf(sampleDogItem, sampleDogItem),
                    isNetworkAvailable = true
                )
            )
            val viewModel = ListViewModel(testDispatcher, useCase)
            advanceUntilIdle()
            Assert.assertTrue(viewModel.actionBarState.value.listOrder == ListOrder.Ascending)

            viewModel.toggleListOrder()
            runCurrent()
            Assert.assertTrue(viewModel.actionBarState.value.listOrder == ListOrder.Descending)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when toggleListLayout is invoked, layout state is toggled`() =
        runTest {
            val useCase = ExploreDogImagesUseCase(
                FakeDogBreedsRepository(
                    cachedDogItems = listOf(sampleDogItem, sampleDogItem),
                    remoteDogItems = listOf(sampleDogItem, sampleDogItem),
                    isNetworkAvailable = true
                )
            )
            val viewModel = ListViewModel(testDispatcher, useCase)
            advanceUntilIdle()
            Assert.assertTrue(viewModel.actionBarState.value.listLayout == ListLayout.List)

            viewModel.toggleListLayout()
            runCurrent()
            Assert.assertTrue(viewModel.actionBarState.value.listLayout == ListLayout.Grid)
        }
    //endregion
}