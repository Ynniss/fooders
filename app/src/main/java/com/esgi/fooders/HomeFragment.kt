package com.esgi.fooders

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.esgi.fooders.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var fabAnalyzeClicked = false
    private var processingBarcode = AtomicBoolean(false)
    private lateinit var cameraExecutor: ExecutorService
    private val scanBarcodeViewModel: ScanBarcodeViewModel by viewModels()
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_animation
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_animation
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_animation
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_animation
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fabAnalyze.setOnClickListener {
                onFabAnalyzeClicked()
            }

            fabManualEntry.setOnClickListener {
                Log.d("fab", "manual entry")
            }
            fabScanBarcode.setOnClickListener {
                Log.d("fab", "scan barcode")
                if (allPermissionsGranted()) {
                    startCamera()
                } else {
                    requestPermissions(
                        REQUIRED_PERMISSIONS,
                        REQUEST_CODE_PERMISSIONS
                    )
                }
            }
        }

        scanBarcodeViewModel.progressState.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        scanBarcodeViewModel.navigation.observe(viewLifecycleOwner, { navDirections ->
            navDirections?.let {
                findNavController().navigate(navDirections)
                scanBarcodeViewModel.doneNavigating()
            }
        })
    }

    private fun onFabAnalyzeClicked() {
        fabAnalyzeClicked = !fabAnalyzeClicked

        binding.fabAnalyze.icon = if (fabAnalyzeClicked) {
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_button_close
            )
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_analysis)
        }

        toggleVisibility()
        setAnimations()
    }

    private fun toggleVisibility() {
        if (fabAnalyzeClicked) {
            binding.fabManualEntry.visibility = View.VISIBLE
            binding.fabScanBarcode.visibility = View.VISIBLE
        } else {
            binding.fabManualEntry.visibility = View.GONE
            binding.fabScanBarcode.visibility = View.GONE

        }
    }

    private fun setAnimations() {
        if (fabAnalyzeClicked) {
            binding.apply {
                fabManualEntry.startAnimation(fromBottom)
                fabScanBarcode.startAnimation(fromBottom)
                fabAnalyze.startAnimation(rotateOpen)
            }
        } else {
            binding.apply {
                fabManualEntry.startAnimation(toBottom)
                fabScanBarcode.startAnimation(toBottom)
                fabAnalyze.startAnimation(rotateClose)
            }
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
                    fragment_scan_barcode_preview_view.surfaceProvider
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
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun searchBarcode(barcode: String) {
        scanBarcodeViewModel.searchBarcode(barcode)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}