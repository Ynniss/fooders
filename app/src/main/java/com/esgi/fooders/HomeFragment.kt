package com.esgi.fooders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.esgi.fooders.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var fabAnalyzeClicked = false

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_animation
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_animation
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_animation
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_animation
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fabAnalyze.setOnClickListener {
                onFabAnalyzeClicked()
            }

            fabManualEntry.setOnClickListener {
                Log.d("fab", "manual entry")
            }
            fabScanBarcode.setOnClickListener {
                Log.d("fab", "scan barcode")
            }
        }
    }

    private fun onFabAnalyzeClicked() {
        fabAnalyzeClicked = !fabAnalyzeClicked

        binding.fabAnalyze.icon = if (fabAnalyzeClicked) {
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_button_close
            )
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_analysis)
        }

        toggleVisibility()
        setAnimations()
    }

    private fun toggleVisibility() {
        if (fabAnalyzeClicked) {
            binding.fabManualEntry.visibility = View.VISIBLE
            binding.fabScanBarcode.visibility = View.VISIBLE
        } else {
            binding.fabManualEntry.visibility = View.GONE
            binding.fabScanBarcode.visibility = View.GONE

        }
    }

    private fun setAnimations() {
        if (fabAnalyzeClicked) {
            binding.apply {
                fabManualEntry.startAnimation(fromBottom)
                fabScanBarcode.startAnimation(fromBottom)
                fabAnalyze.startAnimation(rotateOpen)
            }
        } else {
            binding.apply {
                fabManualEntry.startAnimation(toBottom)
                fabScanBarcode.startAnimation(toBottom)
                fabAnalyze.startAnimation(rotateClose)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}