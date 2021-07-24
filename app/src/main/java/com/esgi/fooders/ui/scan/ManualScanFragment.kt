package com.esgi.fooders.ui.scan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.databinding.FragmentManualScanBinding
import com.esgi.fooders.ui.photo.app.PhotoActivity
import com.esgi.fooders.utils.slideUp
import com.google.android.material.snackbar.Snackbar
import com.tutorialwing.viewpager.VpAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ManualScanFragment : Fragment() {
    private var _binding: FragmentManualScanBinding? = null
    private val binding get() = _binding!!

    val productInfoSharedViewModel: ProductInfoSharedViewModel by hiltNavGraphViewModels(R.id.navigation_graph)

    private val ANIMATION_DURATION = 600L
    private val START_OFFSET = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManualScanBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            Log.d("CLICK", binding.inputBarcode.text.toString())

            productInfoSharedViewModel.getProductInformations(binding.inputBarcode.text.toString())
        }

        lifecycleScope.launchWhenStarted {

            productInfoSharedViewModel.productInformationsEvent.observe(
                viewLifecycleOwner,
                { event ->
                    when (event) {
                        is ProductInfoSharedViewModel.ProductInformationsEvent.Success -> {
                            Log.d("RESULT", event.result.data.toString())
                            refreshUi(failed = false)
                            binding.tabLayout.setupWithViewPager(binding.viewpagerProduct)
                            val vpAdapter = VpAdapter(
                                childFragmentManager
                            )
                            binding.apply {
                                viewpagerProduct.adapter = vpAdapter

                                loadHeaderProductInfo(event.result.data!!)

                                imgProduct.setOnClickListener {
                                    PhotoActivity.start(
                                        requireActivity(),
                                        event.result.data.data.code,
                                        imageField = "front"
                                    )
                                }
                            }
                        }
                        is ProductInfoSharedViewModel.ProductInformationsEvent.Failure -> {
                            refreshUi()

                            Snackbar.make(
                                binding.root,
                                event.error,
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        is ProductInfoSharedViewModel.ProductInformationsEvent.Loading -> {
                            binding.apply {
                                viewHeader.visibility = View.GONE
                                binding.tabLayout.visibility = View.GONE
                                binding.viewpagerProduct.visibility = View.GONE
                                lottieFoodLoading.visibility = View.VISIBLE
                                lottieFoodLoading.playAnimation()

                                btnSearch.isEnabled = false
                                btnSearch.isClickable = false

                                inputBarcode.isEnabled = false
                                inputBarcode.isEnabled = false
                            }
                        }
                        else -> Unit
                    }
                })
        }
    }

    private fun refreshUi(failed: Boolean = true) {
        binding.apply {
            lottieFoodLoading.visibility = View.GONE

            btnSearch.isEnabled = true
            btnSearch.isClickable = true

            inputBarcode.isEnabled = true
            inputBarcode.isEnabled = true

            viewHeader.visibility = View.GONE
            tabLayout.visibility = View.GONE
            viewpagerProduct.visibility = View.GONE

            if (!failed) {
                viewHeader.visibility = View.VISIBLE
                viewHeader.slideUp(ANIMATION_DURATION, START_OFFSET)
                tabLayout.visibility = View.VISIBLE
                viewpagerProduct.visibility = View.VISIBLE
                viewpagerProduct.slideUp(ANIMATION_DURATION, START_OFFSET)
            }
        }
    }

    private fun loadHeaderProductInfo(data: ProductInformationsResponse) {
        binding.apply {
            Glide.with(requireContext())
                .load(data.data.image_front_url)
                .placeholder(R.drawable.not_found)
                .into(imgProduct)

            txtProductName.text = data.data.product_name
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUi()

        if (!binding.inputBarcode.text.toString().isEmpty()) {
            productInfoSharedViewModel.getProductInformations(binding.inputBarcode.text.toString())
        }
    }
}