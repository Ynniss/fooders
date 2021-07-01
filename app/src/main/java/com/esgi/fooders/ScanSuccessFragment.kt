package com.esgi.fooders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.esgi.fooders.databinding.FragmentScanSuccessBinding

class ScanSuccessFragment : Fragment() {
    private var _binding: FragmentScanSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanSuccessBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs: ScanSuccessFragmentArgs by navArgs()

        binding.txtBarcode.text = safeArgs.barcode
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}