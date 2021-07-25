package com.esgi.fooders.data.remote.responses.ProductInformations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductInformationsResponse(
    val data: Data,
    val message: String
) : Parcelable