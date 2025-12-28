package com.vourourou.forklife.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class OpenFoodFactsResponse(
    val code: String,
    val status: Int,
    val status_verbose: String,
    val product: Product?
)

@Parcelize
data class Product(
    val code: String,
    val product_name: String? = "",
    val image_front_url: String? = null,
    val image_ingredients_url: String? = null,
    val image_nutrition_url: String? = null,
    val ingredients_text: String? = null,
    val nutriscore_grade: String? = null,
    val ecoscore_grade: String? = null,
    val nova_group: Int? = null,
    val nutriments: Nutriments? = null,
    val packaging: String? = null
) : Parcelable

@Parcelize
data class Nutriments(
    val energy_kcal_100g: Double? = null,
    val fat_100g: Double? = null,
    val saturated_fat_100g: Double? = null,
    val carbohydrates_100g: Double? = null,
    val sugars_100g: Double? = null,
    val fiber_100g: Double? = null,
    val proteins_100g: Double? = null,
    val salt_100g: Double? = null
) : Parcelable
