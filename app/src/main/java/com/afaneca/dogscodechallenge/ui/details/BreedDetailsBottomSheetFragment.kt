package com.afaneca.dogscodechallenge.ui.details

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.afaneca.dogscodechallenge.R
import com.afaneca.dogscodechallenge.databinding.FragmentBreedDetailsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dumb view. No state or business logic besides binding the data it receives to each view
 */
@AndroidEntryPoint
class BreedDetailsBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBreedDetailsBottomSheetBinding
    private val args: BreedDetailsBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreedDetailsBottomSheetBinding.inflate(inflater, container, false)
        setupViews(args.name, args.group, args.origin, args.temperament)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    private fun setupViews(name: String, group: String, origin: String, temperament: String) {
        val localizedUnknown = getString(R.string.unknown)
        with(binding) {
            tvName.text = name.ifBlank { localizedUnknown }
            tvGroup.text = group.ifBlank { localizedUnknown }
            tvOrigin.text = origin.ifBlank { localizedUnknown }
            tvTemperament.text = temperament.ifBlank { localizedUnknown }
        }
    }
}