package com.afaneca.dogscodechallenge.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afaneca.dogscodechallenge.ui.models.DogImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ListState())
    val state = _state.asStateFlow()

    private val _actionBarState = MutableStateFlow(ListActionBarState())
    val actionBarState = _actionBarState.asStateFlow()

    init {
        getBreedImages()
    }

    private fun getBreedImages() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)
            delay(2000)
            _state.value = _state.value.copy(
                isLoading = false,
                listItems = listOf(
                    DogImage(
                        "1",
                        "Breed 1",
                        "Group 1",
                        "Origin 1",
                        "Temperament 1",
                        "https://cdn2.thedogapi.com/images/SyfsC19NQ_1280.jpg"
                    ),
                    DogImage(
                        "2",
                        "Breed 2",
                        "Group 2",
                        "Origin 2",
                        "Temperament 2",
                        "https://cdn2.thedogapi.com/images/SyfsC19NQ_1280.jpg"
                    ),
                    DogImage(
                        "3",
                        "Breed 3",
                        "Group 3",
                        "Origin 3",
                        "Temperament 3",
                        "https://cdn2.thedogapi.com/images/SyfsC19NQ_1280.jpg"
                    ),
                    DogImage(
                        "4",
                        "Breed 4",
                        "Group 4",
                        "Origin 4",
                        "Temperament 4",
                        "https://cdn2.thedogapi.com/images/SyfsC19NQ_1280.jpg"
                    )
                )
            )
            if (_actionBarState.value.listLayout == ListLayout.None)
                _actionBarState.value = _actionBarState.value.copy(listLayout = ListLayout.List)
        }
    }

    fun toggleListLayout() {
        _actionBarState.value = with(_actionBarState.value) {
            copy(
                listLayout = if (this.listLayout == ListLayout.List) ListLayout.Grid else ListLayout.List
            )
        }
    }
}