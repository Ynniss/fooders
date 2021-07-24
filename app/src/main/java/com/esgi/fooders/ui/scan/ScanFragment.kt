package com.esgi.fooders.ui.scan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.databinding.FragmentScanBinding
import com.esgi.fooders.ui.photo.app.PhotoActivity
import com.esgi.fooders.utils.BarcodeAnalyzer
import com.esgi.fooders.utils.slideUp
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.tutorialwing.viewpager.VpAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private val ANIMATION_DURATION = 600L
    private val START_OFFSET = 0L

    private var processingBarcode = AtomicBoolean(false)
    private lateinit var cameraExecutor: ExecutorService
    private val scanViewModel: ScanViewModel by viewModels()
    //private val productInfoSharedViewModel: ProductInfoSharedViewModel by navGraphViewModels(R.id.navigation_graph) {
    //    SavedStateViewModelFactory(requireActivity().application, requireParentFragment())
    //}

    private lateinit var camera: Camera
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var cameraSelector: CameraSelector
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var preview: Preview
    val productInfoSharedViewModel: ProductInfoSharedViewModel by hiltNavGraphViewModels(R.id.navigation_graph)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val view = binding.root
        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SCAN", productInfoSharedViewModel.isBeenRequestData.value.toString())


        startCamera()

        binding.btnEditProduct.setOnClickListener {
        }

        scanViewModel.progressState.observe(viewLifecycleOwner, { isInProgress ->
            if (isInProgress) {
                cameraProvider.unbindAll()

                binding.apply {
                    lottieFoodLoading.visibility = View.VISIBLE
                    lottieFoodLoading.playAnimation()

                    txtScanLoading.visibility = View.VISIBLE
                }
            }
        })

        scanViewModel.barcode.observe(viewLifecycleOwner, { barcode ->
            lifecycleScope.launch(Main) {
                productInfoSharedViewModel.apply {
                    getProductInformations(barcode!!)
                }
            }

            productInfoSharedViewModel.isBeenRequestData.observe(viewLifecycleOwner, {
                if (it) {
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
                                else -> Unit
                            }
                        })
                }
            })
        })

        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 530
            this.state = BottomSheetBehavior.STATE_EXPANDED
            addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {


                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    Log.d("test", " toto ")
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            startCamera()
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> cameraProvider.unbindAll()
                    }

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    Log.d("OnSlide", "SLIDING")
                }
            })
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build().also {
                it.setSurfaceProvider(
                    binding.fragmentScanBarcodePreviewView.surfaceProvider
                )
            }

            imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                        if (processingBarcode.compareAndSet(false, true)) {
                            scanViewModel.searchBarcode(barcode)
                        }
                    })
                }

            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                camera =
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                if (camera.cameraInfo.hasFlashUnit()) {
                    camera.cameraControl.enableTorch(true)
                }

            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
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

    private fun refreshUi(failed: Boolean = true) {
        binding.apply {
            lottieFoodLoading.visibility = View.GONE
            txtScanLoading.visibility = View.GONE

            if (!failed) {
                layoutBottomSheet.visibility = View.VISIBLE
                layoutBottomSheet.slideUp(ANIMATION_DURATION, START_OFFSET)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        productInfoSharedViewModel.resetBooleanCheck()
        if (!scanViewModel.barcode.value.isNullOrEmpty()) {
            productInfoSharedViewModel.getProductInformations(scanViewModel.barcode.value!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}