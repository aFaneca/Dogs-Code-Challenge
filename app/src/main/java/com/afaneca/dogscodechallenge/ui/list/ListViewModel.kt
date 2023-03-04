package com.afaneca.dogscodechallenge.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.usecase.ExploreDogImagesUseCase
import com.afaneca.dogscodechallenge.ui.model.DogImageUiModel
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
            exploreDogImagesUseCase(_actionBarState.value.listOrder.mapToDomain()).onEach {
                when (it) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            listItems = it.data?.map { item -> DogImageUiModel.mapFromDomain(item) },
                            isLoading = false, error = null
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(isLoading = false, error = it.message)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                }
            }.launchIn(viewModelScope)

            if (_actionBarState.value.listLayout == ListLayout.None)
                _actionBarState.value = _actionBarState.value.copy(listLayout = ListLayout.List)
            if (_actionBarState.value.listOrder == ListOrder.None)
                _actionBarState.value = _actionBarState.value.copy(listOrder = ListOrder.Ascending)
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
        _state.value = _state.value.copy(listItems = null, page = 1)
        getBreedImages()
    }
}