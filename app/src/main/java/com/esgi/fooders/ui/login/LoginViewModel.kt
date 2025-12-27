package com.esgi.fooders.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.fooders.data.repository.LoginRepository
import com.esgi.fooders.utils.DataStoreManager
import com.esgi.fooders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    sealed class LoginEvent {
        class Success(val result: String) : LoginEvent()
        class Failure(val error: String) : LoginEvent()
        data object Loading : LoginEvent()
        data object Empty : LoginEvent()
    }

    private val _loginEventData = MutableLiveData<LoginEvent>(LoginEvent.Empty)
    val loginEvent: LiveData<LoginEvent> = _loginEventData

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    fun login(username: String, password: String, fcmToken: String) {
        viewModelScope.launch(IO) {
            withContext(Main) { _loginEventData.value = LoginEvent.Loading }
            when (val loginResponse = repository.login(username, password, fcmToken)) {
                is Resource.Error ->
                    withContext(Main) {
                        _loginEventData.value =
                            LoginEvent.Failure(loginResponse.message!!)
                    }
                is Resource.Success -> {
                    dataStoreManager.updateUsername(username)
                    withContext(Main) {
                        _loginEventData.value =
                            LoginEvent.Success(loginResponse.data!!.message)
                    }
                }
            }
        }
    }
}
