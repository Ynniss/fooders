package com.vourourou.forklife.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Base skeleton component with animated shimmer effect
 */
@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surfaceVariant
        ),
        start = Offset(shimmerTranslate - 1000f, shimmerTranslate - 1000f),
        end = Offset(shimmerTranslate, shimmerTranslate)
    )

    Box(
        modifier = modifier
            .background(brush = shimmerBrush, shape = shape)
    )
}

/**
 * Skeleton box with specific size
 */
@Composable
fun SkeletonBox(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    SkeletonBox(
        modifier = modifier.size(width = width, height = height),
        shape = shape
    )
}

/**
 * Skeleton box that fills max width
 */
@Composable
fun SkeletonBoxFillWidth(
    height: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    SkeletonBox(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = shape
    )
}
