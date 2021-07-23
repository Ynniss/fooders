package com.esgi.fooders.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esgi.fooders.R
import com.esgi.fooders.databinding.FragmentManualScanBinding


class ManualScanFragment : Fragment(R.layout.fragment_manual_scan) {
    private var _binding: FragmentManualScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManualScanBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }
}