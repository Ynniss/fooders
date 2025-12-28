package com.vourourou.forklife.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Square skeleton placeholder for images
 */
@Composable
fun SkeletonImageSquare(
    size: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    SkeletonBox(
        modifier = modifier.size(size),
        shape = shape
    )
}

/**
 * Rectangular skeleton placeholder for images
 */
@Composable
fun SkeletonImageRect(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    SkeletonBox(
        width = width,
        height = height,
        modifier = modifier,
        shape = shape
    )
}

/**
 * Full width skeleton image
 */
@Composable
fun SkeletonImageFillWidth(
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
