package com.vourourou.forklife.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val ForkLifeShapes = Shapes(
    // Extra small - chips, small buttons
    extraSmall = RoundedCornerShape(8.dp),

    // Small - smaller cards, text fields
    small = RoundedCornerShape(12.dp),

    // Medium - cards, dialogs
    medium = RoundedCornerShape(16.dp),

    // Large - large cards, bottom sheets
    large = RoundedCornerShape(24.dp),

    // Extra large - full screen dialogs, large bottom sheets
    extraLarge = RoundedCornerShape(28.dp)
)

// Additional custom shapes for specific use cases
object ForkLifeCustomShapes {
    val BottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    val TopBar = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
    val Card = RoundedCornerShape(24.dp)
    val CardSmall = RoundedCornerShape(16.dp)
    val Button = RoundedCornerShape(24.dp)
    val TextField = RoundedCornerShape(16.dp)
    val Chip = RoundedCornerShape(8.dp)
    val FAB = RoundedCornerShape(16.dp)
    val Avatar = RoundedCornerShape(50)
    val ProductImage = RoundedCornerShape(12.dp)
    val FullRounded = RoundedCornerShape(50)
}
