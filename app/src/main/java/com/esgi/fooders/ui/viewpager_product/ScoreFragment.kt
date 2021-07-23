package com.esgi.fooders.ui.viewpager_product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.databinding.FragmentScoreBinding
import com.esgi.fooders.ui.scan.ProductInfoSharedViewModel

class ScoreFragment : Fragment() {
    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!

    private val productInfoSharedViewModel: ProductInfoSharedViewModel by navGraphViewModels(R.id.navigation_graph_scan) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScoreBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.root.visibility = View.VISIBLE
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("SCORE", "BEGINNING")

        productInfoSharedViewModel.productInformationsEvent.observe(
            viewLifecycleOwner,
            { event ->
                when (event) {
                    is ProductInfoSharedViewModel.ProductInformationsEvent.Success -> {
                        Log.d("SCORE FRAGMENT", event.result.data.toString())

                        loadUi(event.result.data!!)

                    }
                }
            })
    }

    private fun loadUi(data: ProductInformationsResponse) {
        binding.apply {
            val novaGroupImage = when (data.data.nova_group) {
                1 -> R.drawable.nova_group_1
                2 -> R.drawable.nova_group_2
                3 -> R.drawable.nova_group_3
                4 -> R.drawable.nova_group_4
                else -> R.drawable.not_found
            }
            imgNovaGroup.setBackgroundResource(novaGroupImage)

            val nutriscoreImage = when (data.data.nutriscore_grade) {
                "a" -> R.drawable.nutriscore_a
                "b" -> R.drawable.nutriscore_b
                "c" -> R.drawable.nutriscore_c
                "d" -> R.drawable.nutriscore_d
                "e" -> R.drawable.nutriscore_e
                else -> R.drawable.nutriscore_unknown
            }
            imgNutriscore.setBackgroundResource(nutriscoreImage)


            val ecoScoreImage = when (data.data.ecoscore_grade) {
                "a" -> R.drawable.ecoscore_a
                "b" -> R.drawable.ecoscore_b
                "c" -> R.drawable.ecoscore_c
                "d" -> R.drawable.ecoscore_d
                "e" -> R.drawable.ecoscore_e
                else -> R.drawable.ecoscore_unknown
            }
            toto.setBackgroundResource(ecoScoreImage)
        }

        Log.d("SCORE", "END")
    }

}