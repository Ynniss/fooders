package com.esgi.fooders.ui.profile.viewpager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.fooders.data.remote.FoodersApi
import com.esgi.fooders.data.remote.requests.MiscSuccessEventRequest
import com.esgi.fooders.data.remote.requests.UpdateStatisticRequest
import com.esgi.fooders.data.remote.responses.UserSuccessResponse.Succes
import com.esgi.fooders.data.remote.responses.UserSuccessResponse.UserSuccessResponse
import com.esgi.fooders.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SuccessEventViewModel @Inject constructor(
    private val api: FoodersApi
) : ViewModel() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    fun updateUserStat(statName: String) {
        viewModelScope.launch(IO) {
            try {
                api.updateUserStat(UpdateStatisticRequest(statName, dataStoreManager.readUsername()))
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    fun miscUserSuccess(successEventType: String) {
        viewModelScope.launch(IO) {
            try {
                api.miscSuccessEventRequest( MiscSuccessEventRequest(successEventType, dataStoreManager.readUsername()))
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }
}
