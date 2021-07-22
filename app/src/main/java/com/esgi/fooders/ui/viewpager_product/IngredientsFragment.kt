package com.esgi.fooders.ui.viewpager_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.databinding.FragmentIngredientsBinding
import com.esgi.fooders.ui.scan.ProductInfoSharedViewModel

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private lateinit var productInfoSharedViewModel: ProductInfoSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
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
            Glide.with(requireContext()).load(data.data.image_ingredients_url)
                .into(imgIngredientsList)

            txtIngredients.text = data.data.ingredients_text ?: "Liste des ingrédients non trouvée"
        }
    }
}