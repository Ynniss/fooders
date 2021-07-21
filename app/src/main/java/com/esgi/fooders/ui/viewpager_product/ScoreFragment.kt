package com.esgi.fooders.ui.viewpager_product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.esgi.fooders.databinding.FragmentScoreBinding
import com.esgi.fooders.ui.scan.ProductInfoSharedViewModel

class ScoreFragment : Fragment() {
    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var productInfoSharedViewModel: ProductInfoSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScoreBinding.inflate(inflater, container, false)
        val view = binding.root

        productInfoSharedViewModel =
            ViewModelProvider(requireActivity()).get(ProductInfoSharedViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productInfoSharedViewModel.productInformationsEvent.observe(
            viewLifecycleOwner,
            { event ->
                when (event) {
                    is ProductInfoSharedViewModel.ProductInformationsEvent.Success -> {
                        Log.d("VOILAAAAA", event.result.data.toString())

                        binding.apply {
                            testText.text = event.result.data!!.message
                        }

                    }
                }
            })
    }

}