package com.esgi.fooders.ui.editproduct

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
class EditProductViewModel @Inject constructor(
    private val api: FoodersApi
) : ViewModel() {

    private val productModificationEventData = MutableLiveData<String?>(null)
    val productModificationEvent: LiveData<String?> get() = productModificationEventData

    fun modifyProductInformations(body: MultipartBody) {
        viewModelScope.launch(IO) {
            try {
                val modifyProductInformationsResponse =
                    api.postProductModifications(body)
                Log.d("RESPONSE", modifyProductInformationsResponse.body()?.status.toString())

                when (modifyProductInformationsResponse.body()?.status_verbose.toString()) {
                    "fields saved" -> withContext(Main) {
                        productModificationEventData.value = "STATUS OK"
                    }
                    else -> withContext(Main) {
                        productModificationEventData.value = "STATUS NOT OK"
                    }
                }
                Log.d("RESPONSE", modifyProductInformationsResponse.body()?.toString()!!)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
                withContext(Main) {
                    productModificationEventData.value = "STATUS NOT OK"
                }
            }
        }
    }
}
