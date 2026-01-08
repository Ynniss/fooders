package com.vourourou.forklife.utils

import android.content.Context
import android.content.Intent
import com.vourourou.forklife.R
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.data.local.entity.ScanHistoryItem

/**
 * Utility object for sharing product information.
 * Follows Single Responsibility Principle by encapsulating share-related logic.
 */
object ShareUtils {

    private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.vourourou.forklife"

    /**
     * Generates share text for a Product object from the API.
     */
    fun generateShareText(context: Context, product: Product): String {
        val productName = product.product_name ?: context.getString(R.string.unknown_product)

        return buildShareText(
            context = context,
            productName = productName,
            nutriscoreGrade = product.nutriscore_grade?.uppercase(),
            ecoscoreGrade = product.ecoscore_grade?.uppercase()
        )
    }

    /**
     * Generates share text for a ScanHistoryItem object from the database.
     */
    fun generateShareText(context: Context, historyItem: ScanHistoryItem): String {
        return buildShareText(
            context = context,
            productName = historyItem.productName,
            nutriscoreGrade = historyItem.nutriscoreGrade?.uppercase(),
            ecoscoreGrade = historyItem.ecoscoreGrade?.uppercase()
        )
    }

    /**
     * Builds the share text with the given product information.
     */
    private fun buildShareText(
        context: Context,
        productName: String,
        nutriscoreGrade: String?,
        ecoscoreGrade: String?
    ): String {
        val builder = StringBuilder()

        // Header
        builder.append(context.getString(R.string.share_header, productName))
        builder.append("\n\n")

        // Scores (only if available)
        if (!nutriscoreGrade.isNullOrEmpty()) {
            builder.append(context.getString(R.string.share_nutri_score, nutriscoreGrade))
            builder.append("\n")
        }
        if (!ecoscoreGrade.isNullOrEmpty()) {
            builder.append(context.getString(R.string.share_eco_score, ecoscoreGrade))
            builder.append("\n")
        }

        // Footer
        builder.append("\n")
        builder.append(context.getString(R.string.share_footer))
        builder.append("\n")
        builder.append(PLAY_STORE_URL)

        return builder.toString()
    }

    /**
     * Creates a share Intent with the given text.
     */
    fun createShareIntent(context: Context, shareText: String): Intent {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        return Intent.createChooser(intent, context.getString(R.string.share_product))
    }

    /**
     * Shares a Product via Android share sheet.
     */
    fun shareProduct(context: Context, product: Product) {
        val shareText = generateShareText(context, product)
        val intent = createShareIntent(context, shareText)
        context.startActivity(intent)
    }

    /**
     * Shares a ScanHistoryItem via Android share sheet.
     */
    fun shareHistoryItem(context: Context, historyItem: ScanHistoryItem) {
        val shareText = generateShareText(context, historyItem)
        val intent = createShareIntent(context, shareText)
        context.startActivity(intent)
    }
}
