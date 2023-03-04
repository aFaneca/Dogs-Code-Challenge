package com.afaneca.dogscodechallenge.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afaneca.dogscodechallenge.ui.model.DogImageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private fun resetListState() {
        _state.value = _state.value.copy(listItems = null, page = 0)
    }

    private fun incrementPageNumber() {
        with(_state.value) {
            _state.value = this.copy(page = this.page + 1)
        }
    }

    private fun getSearchResults(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO
            _state.value = _state.value.copy(isLoading = true)
            delay(2000)
            _state.value = _state.value.copy(
                isLoading = false,
                listItems = listOf(
                    DogImageUiModel(
                        "1",
                        "Breed 1",
                        "Group 1",
                        "Origin 1",
                        "Temperament 1",
                        "https://cdn2.thedogapi.com/images/SyfsC19NQ_1280.jpg"
                    ),
                    DogImageUiModel(
                        "2",
                        "Breed 2",
                        "Group 2",
                        "Origin 2",
                        "Temperament 2",
                        "https://cdn2.thedogapi.com/images/SyfsC19NQ_1280.jpg"
                    ),
                    DogImageUiModel(
                        "3",
                        "Breed 3",
                        "Group 3",
                        "Origin 3",
                        "Temperament 3",
                        "https://cdn2.thedogapi.com/images/SyfsC19NQ_1280.jpg"
                    ),
                    DogImageUiModel(
                        "4",
                        "Breed 4",
                        "Group 4",
                        "Origin 4",
                        "Temperament 4",
                        "https://cdn2.thedogapi.com/images/SyfsC19NQ_1280.jpg"
                    )
                )
            )
        }
    }

    fun requestNextPage() {
        // increment page number
        incrementPageNumber()

        // get more items
        _state.value.searchQuery?.let { getSearchResults(it) }
    }

    fun onQuerySubmitted(query: String?) {
        if (query.isNullOrEmpty()) return
        resetListState()
        getSearchResults(query)
    }

}