package com.afaneca.dogscodechallenge.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.usecase.ExploreDogImagesUseCase
import com.afaneca.dogscodechallenge.ui.model.DogItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val exploreDogImagesUseCase: ExploreDogImagesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListState())
    val state = _state.asStateFlow()

    private val _actionBarState = MutableStateFlow(ListActionBarState())
    val actionBarState = _actionBarState.asStateFlow()

    init {
        getBreedImages()
    }

    private fun getBreedImages() {
        viewModelScope.launch(Dispatchers.IO) {
            exploreDogImagesUseCase(
                page = _state.value.page,
                order = _actionBarState.value.listOrder.mapToDomain()
            ).onEach {
                when (it) {
                    is Resource.Success -> {
                        val pageData = it.data?.map { item -> DogItemUiModel.mapFromDomain(item) }
                        if (!pageData.isNullOrEmpty()) {
                            _state.value = _state.value.copy(
                                listItems = _state.value.listItems?.plus(pageData) ?: pageData,
                                isLoading = false,
                                isLoadingFromPagination = false,
                                error = null,
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isLoadingFromPagination = false,
                            error = it.message
                        )
                    }
                    is Resource.Loading -> {
                        if (_state.value.listItems.isNullOrEmpty()) {
                            // First load
                            _state.value = _state.value.copy(isLoading = true, error = null)
                        } else {
                            // Pagination loading
                            _state.value =
                                _state.value.copy(isLoadingFromPagination = true, error = null)
                        }

                    }
                }
            }.launchIn(viewModelScope)

            if (_actionBarState.value.listLayout == ListLayout.None)
                _actionBarState.value = _actionBarState.value.copy(listLayout = ListLayout.List)
            if (_actionBarState.value.listOrder == ListOrder.None)
                _actionBarState.value = _actionBarState.value.copy(listOrder = ListOrder.Ascending)
        }
    }

    private fun resetListState() {
        _state.value = _state.value.copy(listItems = null, page = 0)
    }

    private fun incrementPageNumber() {
        with(_state.value) {
            _state.value = this.copy(page = this.page + 1)
        }
    }

    fun toggleListLayout() {
        _actionBarState.value = with(_actionBarState.value) {
            copy(
                listLayout = if (this.listLayout == ListLayout.List) ListLayout.Grid else ListLayout.List
            )
        }
    }

    fun toggleListOrder() {
        _actionBarState.value = with(_actionBarState.value) {
            copy(
                listOrder = if (this.listOrder == ListOrder.Ascending) ListOrder.Descending else ListOrder.Ascending,
            )
        }
        // reset list
        resetListState()

        // get items
        getBreedImages()
    }

    fun requestNextPage() {
        // increment page number
        incrementPageNumber()

        // get more items
        getBreedImages()
    }
}