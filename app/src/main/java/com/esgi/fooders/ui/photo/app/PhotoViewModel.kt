package com.esgi.fooders.ui.photo.app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.fooders.data.remote.FoodersApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val api: FoodersApi
) : ViewModel() {

    private val imageModificationEventData = MutableLiveData<String?>(null)
    val imageModificationEvent: LiveData<String?> get() = imageModificationEventData

    fun modifyProductImage(body: MultipartBody) {
        viewModelScope.launch(IO) {
            try {
                val modifyProductImageResponse =
                    api.postProductImageModifications(body)
                Log.d("RESPONSE", modifyProductImageResponse.body()?.status.toString())

                when (modifyProductImageResponse.body()?.status.toString()) {
                    "status ok" -> withContext(Main) {
                        imageModificationEventData.value = "STATUS OK"
                    }
                    "status not ok" -> withContext(Main) {
                        imageModificationEventData.value = "STATUS OK"
                    }
                    else -> withContext(Main) {
                        imageModificationEventData.value = "STATUS NOT OK"
                    }
                }
                Log.d("RESPONSE", modifyProductImageResponse.body()?.toString()!!)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
                withContext(Main) {
                    imageModificationEventData.value = "STATUS NOT OK"
                }
            }
        }
    }
}
