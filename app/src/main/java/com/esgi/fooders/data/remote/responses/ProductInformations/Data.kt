package com.esgi.fooders.data.remote.responses.ProductInformations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    val code: String,
    val image_front_url: String,
    val image_ingredients_url: String,
    val image_nutrition_url: String,
    val ingredients_text: String?,
    val nova_group: Int,
    val nutriments: Nutriments,
    var product_name: String = "Product Name Missing",
    val nutriscore_grade: String?,
    val ecoscore_grade: String?,
    val packaging: String?
) : Parcelable