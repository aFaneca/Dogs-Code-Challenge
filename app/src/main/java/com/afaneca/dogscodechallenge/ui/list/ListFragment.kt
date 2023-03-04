package com.afaneca.dogscodechallenge.ui.list

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.afaneca.dogscodechallenge.R
import com.afaneca.dogscodechallenge.databinding.FragmentListBinding
import com.afaneca.dogscodechallenge.ui.models.DogImage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by viewModels()

    private val gridMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(
                R.menu.list_toolbar_menu_grid, menu
            )
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_toggle_view -> {
                    toggleListLayout()
                    true
                }
                else -> false
            }
        }
    }

    private val listMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(
                R.menu.list_toolbar_menu_list, menu
            )
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_toggle_view -> {
                    toggleListLayout()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        observeState()

        return binding.root
    }

    private fun toggleListLayout() {
        viewModel.toggleListLayout()
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                binding.pbLoading.root.isVisible = state.isLoading
                if (!state.listItems.isNullOrEmpty()) {
                    binding.rvList.isVisible = true
                    setupRecyclerView(state.listItems)
                }
            }.launchIn(lifecycleScope)

        viewModel.actionBarState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                refreshActionBar(state.listLayout)
                refreshRecyclerLayoutManager(state.listLayout)
            }.launchIn(lifecycleScope)
    }

    /**
     * Refreshes layout view (1 or 2 columns) for the recycler view according to [ListLayout] state
     */
    private fun refreshRecyclerLayoutManager(listLayout: ListLayout) {
        binding.rvList.layoutManager =
            GridLayoutManager(
                context,
                if (listLayout == ListLayout.List) 1 else 2
            )
    }


    /**
     * Refreshes the visibility of the action bar icons, depending on the current [ListLayout] state
     */
    private fun refreshActionBar(listLayout: ListLayout) {
        when (listLayout) {
            is ListLayout.List -> {
                requireActivity().removeMenuProvider(listMenuProvider)
                requireActivity().removeMenuProvider(gridMenuProvider)
                requireActivity().addMenuProvider(
                    listMenuProvider,
                    viewLifecycleOwner,
                    Lifecycle.State.STARTED
                )
            }
            is ListLayout.Grid -> {
                requireActivity().removeMenuProvider(gridMenuProvider)
                requireActivity().removeMenuProvider(listMenuProvider)
                requireActivity().addMenuProvider(
                    gridMenuProvider,
                    viewLifecycleOwner,
                    Lifecycle.State.STARTED
                )
            }
            else -> {
                requireActivity().removeMenuProvider(listMenuProvider)
                requireActivity().removeMenuProvider(gridMenuProvider)
            }
        }
    }

    private fun setupRecyclerView(list: List<DogImage>) {
        if (binding.rvList.adapter == null) {
            // setup
            binding.rvList.apply {
                adapter = DogListAdapter(context) {}
            }
        }
        // refresh data
        (binding.rvList.adapter as DogListAdapter).submitList(list)
    }
}