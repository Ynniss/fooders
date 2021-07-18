package com.esgi.fooders.ui.scan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.esgi.fooders.databinding.FragmentScanBinding
import com.esgi.fooders.utils.BarcodeAnalyzer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
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

    private var processingBarcode = AtomicBoolean(false)
    private lateinit var cameraExecutor: ExecutorService
    private val scanViewModel: ScanViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val view = binding.root
        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCamera()

        scanViewModel.progressState.observe(viewLifecycleOwner, { isInProgress ->
            if (isInProgress) {
                binding.apply {
                    lottieFoodLoading.visibility = View.VISIBLE
                    lottieFoodLoading.playAnimation()

                    txtScanLoading.visibility = View.VISIBLE
                }
            }
        })

        scanViewModel.barcode.observe(viewLifecycleOwner, { barcode ->
            lifecycleScope.launch(Main) {
                scanViewModel.getProductInformations(barcode!!)
            }

            lifecycleScope.launchWhenStarted {
                scanViewModel.scanEvent.observe(viewLifecycleOwner, { event ->
                    when (event) {
                        is ScanViewModel.ScanEvent.Success -> {
                            refreshUi(failed = false)
                            Log.d("RESULT", event.result.data.toString())
                        }
                        is ScanViewModel.ScanEvent.Failure -> {
                            refreshUi()

                            Snackbar.make(binding.root, event.error, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        else -> Unit
                    }
                })
            }
        })

        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 300
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(
                    binding.fragmentScanBarcodePreviewView.surfaceProvider
                )
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                        if (processingBarcode.compareAndSet(false, true)) {
                            scanViewModel.searchBarcode(barcode)
                        }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                val camera =
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                if (camera.cameraInfo.hasFlashUnit()) {
                    camera.cameraControl.enableTorch(true)
                }

            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun refreshUi(failed: Boolean = true) {
        binding.apply {
            lottieFoodLoading.visibility = View.GONE
            txtScanLoading.visibility = View.GONE

            if (!failed) {
                layoutBottomSheet.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}