package com.esgi.fooders.ui.viewpager_product

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.databinding.FragmentCharacteristicsBinding
import com.esgi.fooders.ui.scan.ProductInfoSharedViewModel


class CharacteristicsFragment : Fragment() {

    private var _binding: FragmentCharacteristicsBinding? = null
    private val binding get() = _binding!!

    private lateinit var productInfoSharedViewModel: ProductInfoSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacteristicsBinding.inflate(inflater, container, false)
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
                        loadUi(event.result.data!!)
                    }
                }
            })
    }

    private fun loadUi(data: ProductInformationsResponse) {
        binding.apply {
            Glide.with(requireContext()).load(data.data.image_nutrition_url)
                .into(imgNutrimentsList)
        }
        val titleText: TextView
        val gridLayout = GridLayout(context)
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS)
        gridLayout.setColumnCount(2)
        gridLayout.setRowCount(3)

        titleText = TextView(context)
        titleText.setText("toto")
        gridLayout.addView(titleText, 0)
        val param: GridLayout.LayoutParams = GridLayout.LayoutParams()
        param.height = GridLayout.LayoutParams.WRAP_CONTENT
        param.width = GridLayout.LayoutParams.WRAP_CONTENT
        param.rightMargin = 5
        param.topMargin = 5
        param.setGravity(Gravity.CENTER)
        param.columnSpec = GridLayout.spec(0)
        param.rowSpec = GridLayout.spec(0)
        titleText.layoutParams = param

        binding.root.addView(gridLayout)
    }
}