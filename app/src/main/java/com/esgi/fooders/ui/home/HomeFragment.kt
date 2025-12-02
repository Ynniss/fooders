package com.esgi.fooders.ui.home

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.esgi.fooders.R
import com.esgi.fooders.databinding.FragmentHomeBinding
import com.esgi.fooders.utils.DataStoreManager
import com.esgi.fooders.utils.ThemeHelper
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

        applyThemeToCards()

        binding.apply {
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

            cardHistoryFeature.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
            }
        }
    }

    private fun applyThemeToCards() {
        lifecycleScope.launch {
            val theme = dataStoreManager.readTheme()

            binding.apply {
                cardScanFeature.background = ThemeHelper.createCardGradient(requireContext(), theme, 0)
                cardHistoryFeature.background = ThemeHelper.createCardGradient(requireContext(), theme, 1)
                cardAbout.background = ThemeHelper.createCardGradient(requireContext(), theme, 2)
                cardStat1.background = ThemeHelper.createCardGradient(requireContext(), theme, 3)
                cardStat2.background = ThemeHelper.createCardGradient(requireContext(), theme, 4)
            }
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