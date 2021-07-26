package com.esgi.fooders.ui.scan.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.databinding.FragmentEnvironmentBinding
import com.esgi.fooders.ui.scan.ProductInfoSharedViewModel

class EnvironmentFragment : Fragment() {

    private var _binding: FragmentEnvironmentBinding? = null
    private val binding get() = _binding!!

    private val productInfoSharedViewModel: ProductInfoSharedViewModel by navGraphViewModels(R.id.navigation_graph) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnvironmentBinding.inflate(inflater, container, false)
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
            val ecoScoreImage = when (data.data.ecoscore_grade) {
                "a" -> R.drawable.ecoscore_a
                "b" -> R.drawable.ecoscore_b
                "c" -> R.drawable.ecoscore_c
                "d" -> R.drawable.ecoscore_d
                "e" -> R.drawable.ecoscore_e
                else -> R.drawable.ecoscore_unknown
            }
            imgEcoscore.setBackgroundResource(ecoScoreImage)

            txtPackaging.text = data.data.packaging ?: "non spécifié"
        }
    }
}