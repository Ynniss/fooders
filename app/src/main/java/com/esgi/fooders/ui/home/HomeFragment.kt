package com.esgi.fooders.ui.home

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.esgi.fooders.R
import com.esgi.fooders.databinding.FragmentHomeBinding
import com.esgi.fooders.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dataStoreManager: DataStoreManager

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

        // Set hero card gradient based on theme
        setHeroCardTheme()

        binding.apply {
            // Scan card click listener
            cardScanFeature.setOnClickListener {
                if (allPermissionsGranted()) {
                    findNavController().navigate(R.id.action_homeFragment_to_scanFragment)
                } else {
                    requestPermissions(
                        REQUIRED_PERMISSIONS,
                        REQUEST_CODE_PERMISSIONS
                    )
                }
            }

            // History card click listener
            cardHistoryFeature.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
            }
        }
    }

    private fun setHeroCardTheme() {
        lifecycleScope.launch {
            val themeValue = dataStoreManager.readTheme()
            
            val gradientDrawable = when (themeValue) {
                "Avocado" -> R.drawable.gradient_green
                "Orange" -> R.drawable.gradient_orange
                "Cherry" -> R.drawable.gradient_red
                else -> R.drawable.gradient_orange
            }
            
            // Find the FrameLayout inside the hero card and set its background
            binding.cardHero.findViewById<View>(R.id.hero_card_background)?.setBackgroundResource(gradientDrawable)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                findNavController().navigate(R.id.action_homeFragment_to_scanFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}