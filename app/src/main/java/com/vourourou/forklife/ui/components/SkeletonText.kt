package com.vourourou.forklife.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Skeleton placeholder for text lines
 */
@Composable
fun SkeletonText(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    widthFraction: Float = 1f
) {
    val height = style.lineHeight.value.dp

    SkeletonBox(
        modifier = if (widthFraction == 1f) {
            modifier.fillMaxWidth().height(height)
        } else {
            modifier.fillMaxWidth(widthFraction).height(height)
        },
        shape = MaterialTheme.shapes.extraSmall
    )
}

/**
 * Skeleton text with specific width
 */
@Composable
fun SkeletonText(
    width: Dp,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val height = style.lineHeight.value.dp

    SkeletonBox(
        modifier = modifier.width(width).height(height),
        shape = MaterialTheme.shapes.extraSmall
    )
}

/**
 * Short skeleton text line (30% width)
 */
@Composable
fun SkeletonTextShort(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    SkeletonText(
        modifier = modifier,
        style = style,
        widthFraction = 0.3f
    )
}

/**
 * Medium skeleton text line (50% width)
 */
@Composable
fun SkeletonTextMedium(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    SkeletonText(
        modifier = modifier,
        style = style,
        widthFraction = 0.5f
    )
}

/**
 * Long skeleton text line (75% width)
 */
@Composable
fun SkeletonTextLong(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    SkeletonText(
        modifier = modifier,
        style = style,
        widthFraction = 0.75f
    )
}
