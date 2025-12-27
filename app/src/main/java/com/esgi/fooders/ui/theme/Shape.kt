package com.esgi.fooders.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val FoodersShapes = Shapes(
    // Extra small - chips, small buttons
    extraSmall = RoundedCornerShape(4.dp),

    // Small - smaller cards, text fields
    small = RoundedCornerShape(8.dp),

    // Medium - cards, dialogs
    medium = RoundedCornerShape(12.dp),

    // Large - large cards, bottom sheets
    large = RoundedCornerShape(16.dp),

    // Extra large - full screen dialogs, large bottom sheets
    extraLarge = RoundedCornerShape(28.dp)
)

// Additional custom shapes for specific use cases
object FoodersCustomShapes {
    val BottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    val TopBar = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
    val Card = RoundedCornerShape(16.dp)
    val CardSmall = RoundedCornerShape(12.dp)
    val Button = RoundedCornerShape(12.dp)
    val TextField = RoundedCornerShape(12.dp)
    val Chip = RoundedCornerShape(8.dp)
    val Avatar = RoundedCornerShape(50)
    val ProductImage = RoundedCornerShape(12.dp)
    val FullRounded = RoundedCornerShape(50)
}
