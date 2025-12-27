package com.esgi.fooders.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Scan : Screen("scan")
    object ManualScan : Screen("manual_scan?barcode={barcode}") {
        fun createRoute(barcode: String = ""): String {
            return if (barcode.isNotEmpty()) {
                "manual_scan?barcode=$barcode"
            } else {
                "manual_scan"
            }
        }
    }
    object EditProduct : Screen("edit_product/{barcode}/{type}") {
        fun createRoute(barcode: String, type: String): String {
            return "edit_product/$barcode/$type"
        }
    }
    object History : Screen("history")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object PhotoCrop : Screen("photo_crop/{barcode}/{imageField}") {
        fun createRoute(barcode: String, imageField: String): String {
            return "photo_crop/$barcode/$imageField"
        }
    }
}

object NavArguments {
    const val BARCODE = "barcode"
    const val TYPE = "type"
    const val IMAGE_FIELD = "imageField"
}
