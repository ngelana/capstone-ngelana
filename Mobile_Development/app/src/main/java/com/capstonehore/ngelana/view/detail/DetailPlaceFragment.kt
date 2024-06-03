package com.capstonehore.ngelana.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.FragmentDetailPlaceBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailPlaceFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailPlaceBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        val place = arguments?.getParcelable<Place>(ARG_PLACE)
        place?.let {
            setupDetailPlace(it)
        } ?: run {
            showToast(getString(R.string.not_available))
        }
    }

    private fun setupDetailPlace(place: Place) {
        binding.apply {
            placeName.text = place.name
            placeDescription.text = place.description
            Glide.with(this@DetailPlaceFragment)
                .load(place.image)
                .centerCrop()
                .into(placeImage)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PLACE = "arg_place"

        fun newInstance(place: Place): DetailPlaceFragment {
            return DetailPlaceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLACE, place)
                }
            }
        }
    }
}