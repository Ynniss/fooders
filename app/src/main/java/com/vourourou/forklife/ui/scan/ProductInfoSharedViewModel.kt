package com.vourourou.forklife.ui.scan

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.data.repository.HistoryRepository
import com.vourourou.forklife.data.repository.OpenFoodFactsRepository
import com.vourourou.forklife.utils.DataStoreManager
import com.vourourou.forklife.utils.InAppReviewManager
import com.vourourou.forklife.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductInfoSharedViewModel @Inject constructor(
    private val repository: OpenFoodFactsRepository,
    private val dataStoreManager: DataStoreManager,
    private val historyRepository: HistoryRepository,
    private val inAppReviewManager: InAppReviewManager
) : ViewModel() {

    sealed class ProductInformationsEvent {
        class Success(val result: Resource<Product>) :
            ProductInformationsEvent()

        class Failure(val error: String) : ProductInformationsEvent()
        object Loading : ProductInformationsEvent()
        object Empty : ProductInformationsEvent()
    }


    private var _productInformationsEvent =
        MutableLiveData<ProductInformationsEvent>(ProductInformationsEvent.Empty)
    val productInformationsEvent: LiveData<ProductInformationsEvent> = _productInformationsEvent


    private var _isBeenRequestData =
        MutableLiveData(false)
    val isBeenRequestData: LiveData<Boolean> = _isBeenRequestData

    // Selected allergens for allergen detection
    val selectedAllergens: StateFlow<Set<String>> = dataStoreManager.selectedAllergensFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )


    fun getProductInformations(barcode: String, forceRefresh: Boolean = false) {
        viewModelScope.launch(IO) {
            withContext(Main) {
                _productInformationsEvent.value = ProductInformationsEvent.Loading
            }

            // Try to load from cache first (offline-first approach)
            if (!forceRefresh) {
                val cachedProduct = historyRepository.getProductByBarcode(barcode)
                if (cachedProduct != null) {
                    withContext(Main) {
                        Log.d("VM", "Loaded product from cache (offline)")
                        _isBeenRequestData.value = true
                        _productInformationsEvent.value = ProductInformationsEvent.Success(
                            Resource.Success(cachedProduct)
                        )
                    }
                    return@launch
                }
            }

            // If not in cache or force refresh, fetch from API
            when (val result = repository.getProduct(barcode)) {
                is Resource.Success -> withContext(Main) {
                    Log.d("RES SUCCESS", "INSIDE IT")
                    _isBeenRequestData.value = true
                    _productInformationsEvent.value = ProductInformationsEvent.Success(result)
                }
                is Resource.Error -> withContext(Main) {
                    // Note: !! is necessary here despite lint warning, as smart cast doesn't work across coroutine boundary
                    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
                    val errorMessage = result.message!!
                    Log.e("VM", errorMessage)
                    _isBeenRequestData.value = true
                    _productInformationsEvent.value =
                        ProductInformationsEvent.Failure(errorMessage)
                }
            }
        }
    }

    fun resetBooleanCheck() {
        _isBeenRequestData.value = false
    }

    fun trackScan(activity: Activity, productData: Product? = null) {
        viewModelScope.launch(IO) {
            try {
                // Save to history if product data is available
                productData?.let {
                    historyRepository.insertScanHistory(it)
                }

                // Increment scan count
                dataStoreManager.incrementScanCount()

                val scanCount = dataStoreManager.getScanCount()
                Log.d("ProductInfoVM", "Scan tracked: $scanCount")

                // Request in-app review if eligible (after successful scan)
                requestInAppReviewIfEligible(activity)
            } catch (e: Exception) {
                Log.e("ProductInfoVM", "Error tracking scan", e)
            }
        }
    }

    /**
     * Requests an in-app review if the user is eligible.
     * This is triggered after a successful product scan.
     *
     * Eligibility criteria:
     * - At least 5 successful scans
     * - At least 30 days since last review request
     */
    private suspend fun requestInAppReviewIfEligible(activity: Activity) {
        try {
            inAppReviewManager.requestReviewIfEligible(
                activity = activity,
                coroutineScope = viewModelScope,
                onComplete = { success ->
                    Log.d("ProductInfoVM", "In-app review flow completed: $success")
                }
            )
        } catch (e: Exception) {
            // Silently fail - don't interrupt user experience for review errors
            Log.e("ProductInfoVM", "Error requesting in-app review", e)
        }
    }
}
