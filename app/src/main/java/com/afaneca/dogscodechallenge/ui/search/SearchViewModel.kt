package com.afaneca.dogscodechallenge.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afaneca.dogscodechallenge.common.Resource
import com.afaneca.dogscodechallenge.domain.usecase.SearchDogBreedsUseCase
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
class SearchViewModel @Inject constructor(
    private val searchDogBreedsUseCase: SearchDogBreedsUseCase
) : ViewModel() {

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
            searchDogBreedsUseCase(
                query = searchQuery,
                page = _state.value.page
            ).onEach {
                when (it) {
                    is Resource.Success -> {
                        val pageData = it.data?.map { item -> DogItemUiModel.mapFromDomain(item) }
                        if (pageData != null) {
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
                            error = it.message,
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
        _state.value = _state.value.copy(searchQuery = query)
        resetListState()
        getSearchResults(query)
    }

}