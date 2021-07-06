package com.esgi.fooders

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
import androidx.navigation.fragment.findNavController
import com.esgi.fooders.databinding.FragmentScanBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean


class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var processingBarcode = AtomicBoolean(false)
    private lateinit var cameraExecutor: ExecutorService
    private val scanBarcodeViewModel: ScanBarcodeViewModel by viewModels()


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

        scanBarcodeViewModel.progressState.observe(viewLifecycleOwner, { isInProgress ->
            if (isInProgress) {
                binding.apply {
                    lottieFoodLoading.visibility = View.VISIBLE
                    lottieFoodLoading.playAnimation()

                    txtScanLoading.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    lottieFoodLoading.visibility = View.GONE
                    txtScanLoading.visibility = View.GONE
                }
            }
        })

        scanBarcodeViewModel.resultsReceived.observe(viewLifecycleOwner, { resultsReceived ->
            binding.layoutBottomSheet.visibility = View.VISIBLE
        })

        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 300
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }

    private fun startCamera() {
        // Create an instance of the ProcessCameraProvider,
        // which will be used to bind the use cases to a lifecycle owner.
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        // Add a listener to the cameraProviderFuture.
        // The first argument is a Runnable, which will be where the magic actually happens.
        // The second argument (way down below) is an Executor that runs on the main thread.
        cameraProviderFuture.addListener({
            // Add a ProcessCameraProvider, which binds the lifecycle of your camera to
            // the LifecycleOwner within the application's life.
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Initialize the Preview object, get a surface provider from your PreviewView,
            // and set it on the preview instance.
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(
                    binding.fragmentScanBarcodePreviewView.surfaceProvider
                )
            }

            // Setup the ImageAnalyzer for the ImageAnalysis use case
            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                        if (processingBarcode.compareAndSet(false, true)) {
                            searchBarcode(barcode)
                        }
                    })
                }

            // Select back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind any bound use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to lifecycleOwner
                val camera =
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                if (camera.cameraInfo.hasFlashUnit()) {
                    camera.cameraControl.enableTorch(false)
                }

            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun searchBarcode(barcode: String) {
        scanBarcodeViewModel.searchBarcode(barcode)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}