package com.esgi.fooders.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodersTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        scrolledContainerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface
    )
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigate back"
                    )
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = colors
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodersCenterAlignedTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigate back"
                    )
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodersMediumTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    MediumTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigate back"
                    )
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodersLargeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    LargeTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigate back"
                    )
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodersBackTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    FoodersTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationClick = onBackClick,
        actions = actions
    )
}

data class OverflowMenuItem(
    val title: String,
    val onClick: () -> Unit
)

@Composable
fun OverflowMenu(
    items: List<OverflowMenuItem>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = true },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More options"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.title) },
                onClick = {
                    expanded = false
                    item.onClick()
                }
            )
        }
    }
}
