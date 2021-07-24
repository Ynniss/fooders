package com.esgi.fooders.ui.photo.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity
import com.esgi.fooders.databinding.PhotoActivityBinding
import com.esgi.fooders.ui.photo.domain.PhotoContract
import com.esgi.fooders.ui.photo.presenter.PhotoPresenter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@AndroidEntryPoint
internal class PhotoActivity : CropImageActivity(), PhotoContract.View {

    companion object {
        fun start(activity: Activity, barcode: String, imageField: String) {
            val intent = Intent(activity, PhotoActivity::class.java)
            intent.putExtra("barcode", barcode)
            intent.putExtra("imageField", imageField)

            ActivityCompat.startActivity(
                activity,
                intent,
                null
            )
        }
    }

    private val photoViewModel: PhotoViewModel by viewModels()

    private lateinit var binding: PhotoActivityBinding
    private val presenter: PhotoContract.Presenter = PhotoPresenter()

    private lateinit var barcode: String
    private lateinit var imageField: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = PhotoActivityBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        presenter.bindView(this)


        barcode = intent.getStringExtra("barcode")!!
        imageField = intent.getStringExtra("imageField")!!

        Log.d("barcode", barcode)
        Log.d("imageField", imageField)


        binding.saveBtn.setOnClickListener {
            cropImage() // CropImageActivity.cropImage()
        }

        setCropImageView(binding.cropImageView)
    }

    override fun setContentView(view: View) {
        // Override this to use your custom layout
        super.setContentView(binding.root)
    }

    override fun onDestroy() {
        presenter.unbindView()
        super.onDestroy()
    }

    override fun rotate(counter: Int) {
        binding.cropImageView.rotateImage(counter)
    }

    override fun updateRotationCounter(counter: String) {
        //binding.rotateText.text = getString(R.string.rotation_value, counter)
    }

    override fun onPickImageResult(resultUri: Uri?) {
        super.onPickImageResult(resultUri)

        if (resultUri != null) {
            binding.cropImageView.setImageUriAsync(resultUri)
        }
    }

    // Override this to add more information into the intent
    override fun getResultIntent(uri: Uri?, error: java.lang.Exception?, sampleSize: Int): Intent {
        val result = super.getResultIntent(uri, error, sampleSize)
        return result.putExtra("EXTRA_KEY", "Extra data")
    }

    override fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        val result = CropImage.ActivityResult(
            binding.cropImageView.imageUri,
            uri,
            error,
            binding.cropImageView.cropPoints,
            binding.cropImageView.cropRect,
            binding.cropImageView.rotatedDegrees,
            binding.cropImageView.wholeImageRect,
            sampleSize
        )

        Log.v("File Path", result.getUriFilePath(this).toString())

        val photo = File(result.getUriFilePath(this)!!)

        val fileBody: RequestBody =
            RequestBody.create(MediaType.parse(contentResolver.getType(uri!!)!!), photo)

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("imagefield", imageField)
            .addFormDataPart("code", barcode)
            .addFormDataPart("imgupload_front", System.currentTimeMillis().toString(), fileBody)
            .build()

        photoViewModel.modifyProductImage(body)

        photoViewModel.imageModificationEvent.observe(this, { imageModificationEvent ->
            if (imageModificationEvent == "STATUS OK") {
                finish()
            } else if (imageModificationEvent == "STATUS NOT OK") {
                Snackbar.make(
                    binding.root,
                    "An error occured. Please retry.",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    override fun setResultCancel() {
        Log.i("extend", "User this override to change behaviour when cancel")
        super.setResultCancel()
    }

    override fun updateMenuItemIconColor(menu: Menu, itemId: Int, color: Int) {
        Log.i(
            "extend",
            "If not using your layout, this can be one option to change colours. Check README and wiki for more"
        )
        super.updateMenuItemIconColor(menu, itemId, color)
    }
}
