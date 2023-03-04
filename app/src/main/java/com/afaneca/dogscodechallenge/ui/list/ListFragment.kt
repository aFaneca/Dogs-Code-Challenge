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
import androidx.recyclerview.widget.RecyclerView
import com.afaneca.dogscodechallenge.R
import com.afaneca.dogscodechallenge.databinding.FragmentListBinding
import com.afaneca.dogscodechallenge.ui.model.DogImageUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by viewModels()

    private val listMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(
                R.menu.list_toolbar_menu, menu
            )
        }

        override fun onPrepareMenu(menu: Menu) {
            super.onPrepareMenu(menu)
            with(viewModel.actionBarState) {
                // List Layout
                when (this.value.listLayout) {
                    is ListLayout.List -> {
                        menu.findItem(R.id.action_toggle_view)
                            .isVisible = true
                        menu.findItem(R.id.action_toggle_view)
                            .setIcon(R.drawable.ic_dashboard_white_24dp)
                    }
                    is ListLayout.Grid -> {
                        menu.findItem(R.id.action_toggle_view)
                            .isVisible = true
                        menu.findItem(R.id.action_toggle_view)
                            .setIcon(R.drawable.ic_reorder)
                    }
                    else -> {
                        menu.findItem(R.id.action_toggle_view)
                            .isVisible = false
                    }
                }

                // List Order
                when (this.value.listOrder) {
                    is ListOrder.Ascending -> {
                        menu.findItem(R.id.action_toggle_order).isVisible = true
                        menu.findItem(R.id.action_toggle_order).setIcon(R.drawable.ic_move_up)
                    }
                    is ListOrder.Descending -> {
                        menu.findItem(R.id.action_toggle_order).isVisible = true
                        menu.findItem(R.id.action_toggle_order).setIcon(R.drawable.ic_move_down)
                    }
                    else -> {
                        menu.findItem(R.id.action_toggle_order).isVisible = false
                    }
                }
            }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_toggle_view -> {
                    toggleListLayout()
                    true
                }
                R.id.action_toggle_order -> {
                    toggleListOrder()
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

    override fun onResume() {
        super.onResume()
        requireActivity().addMenuProvider(listMenuProvider)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().removeMenuProvider(listMenuProvider)
    }

    private fun toggleListLayout() {
        viewModel.toggleListLayout()
    }

    private fun toggleListOrder() {
        viewModel.toggleListOrder()
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                binding.pbLoading.root.isVisible = state.isLoading
                binding.pbPaginationLoading.isVisible = state.isLoadingFromPagination
                if (!state.listItems.isNullOrEmpty()) {
                    binding.rvList.isVisible = true
                    setupRecyclerView(state.listItems)
                }
            }.launchIn(lifecycleScope)

        viewModel.actionBarState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                refreshActionBar()
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
     * Invalidates current options menu state, forcing a refresh of the [listMenuProvider] state
     */
    private fun refreshActionBar() {
        requireActivity().invalidateOptionsMenu()
    }

    private fun setupRecyclerView(list: List<DogImageUiModel>) {
        if (binding.rvList.adapter == null) {
            // setup
            binding.rvList.apply {
                adapter = DogListAdapter(ListViewType.ExpandedWithImage) {
                    // TODO - click listener to details
                }
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