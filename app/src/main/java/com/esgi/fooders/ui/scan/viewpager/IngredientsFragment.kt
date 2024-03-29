package com.esgi.fooders.ui.scan.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.databinding.FragmentIngredientsBinding
import com.esgi.fooders.ui.photo.app.PhotoActivity
import com.esgi.fooders.ui.scan.ProductInfoSharedViewModel

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val productInfoSharedViewModel: ProductInfoSharedViewModel by navGraphViewModels(R.id.navigation_graph) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.root.visibility = View.VISIBLE

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            productInfoSharedViewModel.isBeenRequestData.observe(viewLifecycleOwner, {
                if (it) {

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
            })
        }
    }

    private fun loadUi(data: ProductInformationsResponse) {
        binding.apply {
            Glide.with(requireContext()).load(data.data.image_ingredients_url)
                .error(R.drawable.not_found)
                .into(imgIngredientsList)

            txtIngredients.text = data.data.ingredients_text ?: "Liste des ingrédients non trouvée"

            imgIngredientsList.setOnClickListener {
                PhotoActivity.start(
                    requireActivity(),
                    data.data.code,
                    imageField = "ingredients"
                )
            }
        }
    }
}