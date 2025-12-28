package com.vourourou.forklife.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vourourou.forklife.ui.theme.ForkLifeCustomShapes

/**
 * Extended FAB with icon + text
 * Google Calendar/Files style
 */
@Composable
fun ForkLifeExtendedFAB(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = ForkLifeCustomShapes.FAB,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        expanded = expanded,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    )
}

/**
 * Regular FAB in various sizes
 */
@Composable
fun ForkLifeFAB(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ForkLifeFABSize = ForkLifeFABSize.Regular
) {
    when (size) {
        ForkLifeFABSize.Small -> {
            SmallFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                shape = ForkLifeCustomShapes.FAB,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 6.dp
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        ForkLifeFABSize.Regular -> {
            FloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                shape = ForkLifeCustomShapes.FAB,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 6.dp
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        ForkLifeFABSize.Large -> {
            LargeFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                shape = ForkLifeCustomShapes.FAB,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 6.dp
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

enum class ForkLifeFABSize {
    Small,
    Regular,
    Large
}
