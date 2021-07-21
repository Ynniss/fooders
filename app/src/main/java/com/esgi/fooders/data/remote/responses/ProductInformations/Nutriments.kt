package com.esgi.fooders.data.remote.responses.ProductInformations

import com.google.gson.annotations.SerializedName

data class Nutriments(
    val carbohydrates: Double,
    val carbohydrates_100g: Double,
    val carbohydrates_unit: String,
    val carbohydrates_value: Double,
    val energy: Int,
    @SerializedName("energy_kcal")
    val energy_kcal: Float,
    @SerializedName("energy-kcal_100g")
    val energy_kcal_100g: Float,
    @SerializedName("energy-kcal_unit")
    val energy_kcal_unit: String,
    @SerializedName("energy-kcal_value")
    val energy_kcal_value: Float,
    @SerializedName("energy-kj")
    val energy_kj: Int,
    @SerializedName("energy-kj_100g")
    val energy_kj_100g: Int,
    @SerializedName("energy-kj_unit")
    val energy_kj_unit: String,
    @SerializedName("energy-kj_value")
    val energy_kj_value: Int,
    val energy_100g: Int,
    val energy_unit: String,
    val energy_value: Float,
    val fat: Float,
    val fat_100g: Float,
    val fat_unit: String,
    val fat_value: Float,
    val fiber: Int,
    val fiber_100g: Int,
    val fiber_unit: String,
    val fiber_value: Int,
    @SerializedName("fruits-vegetables-nuts-estimate-from-ingredients_100g")
    val fruits_vegetables_nuts_estimate_from_ingredients_100g: Float,
    @SerializedName("nova-group")
    val nova_group: Int,
    @SerializedName("nova-group_100g")
    val nova_group_100g: Int,
    @SerializedName("nova-group_serving")
    val nova_group_serving: Int,
    @SerializedName("nutrition-score-fr")
    val nutrition_score_fr: Int,
    @SerializedName("nutrition-score-fr_100g")
    val nutrition_score_fr_100g: Int,
    val proteins: Float,
    val proteins_100g: Float,
    val proteins_unit: String,
    val proteins_value: Float,
    val salt: Float,
    val salt_100g: Float,
    val salt_unit: String,
    val salt_value: Float,
    @SerializedName("saturated-fat")
    val saturated_fat: Float,
    @SerializedName("saturated-fat_100g")
    val saturated_fat_100g: Float,
    @SerializedName("saturated-fat_unit")
    val saturated_fat_unit: String,
    @SerializedName("saturated-fat_value")
    val saturated_fat_value: Float,
    val sugars_serving: Int?,
    val sodium: Float,
    val sodium_100g: Float,
    val sodium_unit: String,
    val sodium_value: Float,
    val sugars: Double,
    val sugars_100g: Double,
    val sugars_unit: String,
    val sugars_value: Double
)