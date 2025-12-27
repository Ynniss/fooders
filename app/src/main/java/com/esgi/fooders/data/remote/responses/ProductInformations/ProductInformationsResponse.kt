package com.esgi.fooders.data.remote.responses.ProductInformations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductInformationsResponse(
    val data: Data,
    val message: String
) : Parcelable