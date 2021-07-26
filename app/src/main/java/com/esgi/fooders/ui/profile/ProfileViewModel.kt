package com.esgi.fooders.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.fooders.data.remote.FoodersApi
import com.esgi.fooders.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val api: FoodersApi
) : ViewModel() {

    private val userSuccessEventData = MutableLiveData<List<String>>(listOf("init"))
    val userSuccessEvent: LiveData<List<String>> get() = userSuccessEventData

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    fun getUserSuccess() {
        viewModelScope.launch(IO) {
            try {

                val userSuccessResponse =
                    api.getUserSuccess(dataStoreManager.readUsername())
                Log.d("RESPONSE", userSuccessResponse.body()?.toString() ?: "NOTHING")

                when (userSuccessResponse.body()?.data?.unlocked) {
                    null -> withContext(Main) {
                        userSuccessEventData.value = listOf("An error occurred")
                    }
                    else -> withContext(Main) {
                        userSuccessEventData.value = userSuccessResponse.body()?.data?.unlocked
                    }
                }
                Log.d("RESPONSE", userSuccessResponse.body()?.toString()!!)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
                withContext(Main) {
                    userSuccessEventData.value = listOf("An error occurred")
                }
            }
        }
    }
}
