package com.esgi.fooders.data.remote.responses.ProductInformations

data class Data(
    val image_front_url: String,
    val image_ingredients_url: String,
    val image_nutrition_url: String,
    val ingredients_text: String,
    val nova_group: Int,
    val nutriments: Nutriments,
    var product_name: String = "Product Name Missing",
    val properties: String
)