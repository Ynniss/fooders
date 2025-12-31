package com.vourourou.forklife.ui.navigation

sealed class Screen(val route: String) {
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
    object ProductDetail : Screen("product_detail/{barcode}") {
        fun createRoute(barcode: String): String = "product_detail/$barcode"
    }
    object History : Screen("history")
    object Stats : Screen("stats")
    object Settings : Screen("settings")
}

object NavArguments {
    const val BARCODE = "barcode"
}
