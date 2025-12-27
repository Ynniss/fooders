package com.esgi.fooders.ui.scan.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.databinding.FragmentCharacteristicsBinding
import com.esgi.fooders.ui.photo.app.PhotoActivity
import com.esgi.fooders.ui.scan.ProductInfoSharedViewModel


class CharacteristicsFragment : Fragment() {

    private var _binding: FragmentCharacteristicsBinding? = null
    private val binding get() = _binding!!

    private val productInfoSharedViewModel: ProductInfoSharedViewModel by navGraphViewModels(R.id.navigation_graph) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacteristicsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.root.visibility = View.VISIBLE

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productInfoSharedViewModel.isBeenRequestData.observe(viewLifecycleOwner, {
            if (it) {

                productInfoSharedViewModel.productInformationsEvent.observe(
                    viewLifecycleOwner,
                    { event ->
                        when (event) {
                            is ProductInfoSharedViewModel.ProductInformationsEvent.Success -> {
                                loadUi(event.result.data!!)
                            }
                            else -> Unit
                        }
                    })
            }
        })
    }

    private fun loadUi(data: ProductInformationsResponse) {
        binding.apply {
            Glide.with(requireContext()).load(data.data.image_nutrition_url)
                .into(imgNutrimentsList)

            txtEnergyKj.text = data.data.nutriments.energy_kj.toString()
            txtFat100g.text = data.data.nutriments.fat_100g.toString()
            txtSaltValue.text = data.data.nutriments.salt_value.toString()
            txtSugars100g.text = data.data.nutriments.sugars_100g.toString()

            imgNutrimentsList.setOnClickListener {
                PhotoActivity.start(
                    requireActivity(),
                    data.data.code,
                    imageField = "nutrition"
                )
            }
        }
    }
}