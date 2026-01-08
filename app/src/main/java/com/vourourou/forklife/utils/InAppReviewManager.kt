package com.vourourou.forklife.utils

import android.app.Activity
import android.util.Log
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Helper class for managing Google Play In-App Reviews.
 *
 * Best practices implemented:
 * - Only triggers after meaningful interactions (5+ successful scans)
 * - Rate limits review requests (max once per 30 days)
 * - Handles errors gracefully without interrupting user experience
 *
 * Note: The actual review dialog may not always appear - Google Play controls
 * when the dialog is shown based on quota and other factors.
 */
class InAppReviewManager @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    companion object {
        private const val TAG = "InAppReviewManager"

        // Minimum number of successful scans before showing review prompt
        const val MIN_SCAN_COUNT_FOR_REVIEW = 5

        // Minimum time between review requests (30 days in milliseconds)
        const val MIN_DAYS_BETWEEN_REVIEWS = 30L
        private const val MILLIS_PER_DAY = 24 * 60 * 60 * 1000L
        val MIN_TIME_BETWEEN_REVIEWS = MIN_DAYS_BETWEEN_REVIEWS * MILLIS_PER_DAY
    }

    /**
     * Checks if the user is eligible for a review prompt based on:
     * - Scan count threshold (minimum 5 successful scans)
     * - Time since last review request (minimum 30 days)
     *
     * @return true if the user should be prompted for a review
     */
    suspend fun shouldRequestReview(): Boolean {
        return withContext(Dispatchers.IO) {
            val scanCount = dataStoreManager.getScanCount()
            val lastReviewTimestamp = dataStoreManager.getLastReviewRequestTimestamp()
            val currentTime = System.currentTimeMillis()

            val hasEnoughScans = scanCount >= MIN_SCAN_COUNT_FOR_REVIEW
            val hasEnoughTimePassed = (currentTime - lastReviewTimestamp) >= MIN_TIME_BETWEEN_REVIEWS

            Log.d(TAG, "Review eligibility check - Scans: $scanCount, LastReview: $lastReviewTimestamp, " +
                    "HasEnoughScans: $hasEnoughScans, HasEnoughTimePassed: $hasEnoughTimePassed")

            hasEnoughScans && hasEnoughTimePassed
        }
    }

    /**
     * Requests and launches the in-app review flow if the user is eligible.
     *
     * This method:
     * 1. Checks eligibility (scan count and time since last request)
     * 2. Requests the review info from Google Play
     * 3. Launches the review flow
     * 4. Updates the last request timestamp
     *
     * Note: The actual review dialog may not appear even if this succeeds,
     * as Google Play controls the display based on quota and other factors.
     *
     * @param activity The activity to launch the review flow from
     * @param coroutineScope The coroutine scope to use for background operations
     * @param onComplete Optional callback when the review flow completes (success or failure)
     */
    suspend fun requestReviewIfEligible(
        activity: Activity,
        coroutineScope: CoroutineScope,
        onComplete: ((success: Boolean) -> Unit)? = null
    ) {
        // Check eligibility first
        if (!shouldRequestReview()) {
            Log.d(TAG, "User not eligible for review prompt")
            onComplete?.invoke(false)
            return
        }

        try {
            launchReviewFlow(activity, coroutineScope, onComplete)
        } catch (e: Exception) {
            Log.e(TAG, "Error requesting review", e)
            onComplete?.invoke(false)
        }
    }

    /**
     * Forces the review flow to launch without eligibility checks.
     * Use this for testing or when you want to bypass the normal conditions.
     *
     * @param activity The activity to launch the review flow from
     * @param coroutineScope The coroutine scope to use for background operations
     * @param onComplete Optional callback when the review flow completes
     */
    suspend fun forceRequestReview(
        activity: Activity,
        coroutineScope: CoroutineScope,
        onComplete: ((success: Boolean) -> Unit)? = null
    ) {
        try {
            launchReviewFlow(activity, coroutineScope, onComplete)
        } catch (e: Exception) {
            Log.e(TAG, "Error forcing review", e)
            onComplete?.invoke(false)
        }
    }

    private suspend fun launchReviewFlow(
        activity: Activity,
        coroutineScope: CoroutineScope,
        onComplete: ((success: Boolean) -> Unit)?
    ) {
        withContext(Dispatchers.Main) {
            val reviewManager = ReviewManagerFactory.create(activity)

            val request = reviewManager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    Log.d(TAG, "Review info obtained successfully")

                    val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
                    flow.addOnCompleteListener { reviewTask ->
                        // Update timestamp regardless of whether user actually reviewed
                        // We don't know if they reviewed - Google doesn't tell us
                        coroutineScope.launch(Dispatchers.IO) {
                            dataStoreManager.updateLastReviewRequestTimestamp(System.currentTimeMillis())
                        }

                        if (reviewTask.isSuccessful) {
                            Log.d(TAG, "Review flow completed")
                            onComplete?.invoke(true)
                        } else {
                            Log.e(TAG, "Review flow failed", reviewTask.exception)
                            onComplete?.invoke(false)
                        }
                    }
                } else {
                    Log.e(TAG, "Failed to get review info", task.exception)
                    onComplete?.invoke(false)
                }
            }
        }
    }

    /**
     * Resets the review request timestamp for testing purposes.
     * This allows the review prompt to appear again immediately.
     */
    suspend fun resetReviewTimestamp() {
        dataStoreManager.updateLastReviewRequestTimestamp(0L)
        Log.d(TAG, "Review timestamp reset")
    }
}
