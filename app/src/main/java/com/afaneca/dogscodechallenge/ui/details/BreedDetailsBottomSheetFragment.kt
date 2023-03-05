package com.afaneca.dogscodechallenge.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.afaneca.dogscodechallenge.R
import com.afaneca.dogscodechallenge.databinding.FragmentBreedDetailsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

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