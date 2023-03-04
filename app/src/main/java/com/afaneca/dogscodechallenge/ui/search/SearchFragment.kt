package com.afaneca.dogscodechallenge.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afaneca.dogscodechallenge.R
import com.afaneca.dogscodechallenge.databinding.FragmentSearchBinding
import com.afaneca.dogscodechallenge.ui.list.DogListAdapter
import com.afaneca.dogscodechallenge.ui.list.ListViewType
import com.afaneca.dogscodechallenge.ui.model.DogImageUiModel
import com.afaneca.dogscodechallenge.ui.utils.hideSoftKeyboard
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private val searchMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(
                R.menu.search_toolbar_menu, menu
            )
            val searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.onQuerySubmitted(query)
                    hideKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    }

    private fun hideKeyboard() {
        requireActivity().hideSoftKeyboard()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        observeState()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().addMenuProvider(searchMenuProvider)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().removeMenuProvider(searchMenuProvider)
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                // Loading
                binding.pbLoading.root.isVisible = state.isLoading
                binding.pbPaginationLoading.isVisible = state.isLoadingFromPagination

                // Empty Views
                binding.emptyView.root.isVisible = !state.isLoading && state.listItems == null
                binding.emptyViewNoResults.root.isVisible =
                    !state.isLoading && state.listItems != null && state.listItems.isEmpty()

                // Recycler View
                binding.rvList.isVisible = !state.listItems.isNullOrEmpty()
                state.listItems?.let { setupRecyclerView(it) }
            }.launchIn(lifecycleScope)
    }

    private fun setupRecyclerView(list: List<DogImageUiModel>) {
        if (binding.rvList.adapter == null) {
            // setup
            binding.rvList.apply {
                adapter = DogListAdapter(ListViewType.CollapsedWithInfo) {
                    // TODO - click listener to details
                }
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!recyclerView.canScrollVertically(1) && dy > 0) {
                            //scrolled to BOTTOM
                            viewModel.requestNextPage()
                        }
                    }
                })
            }
        }
        // refresh data
        (binding.rvList.adapter as DogListAdapter).submitList(list)
    }
}