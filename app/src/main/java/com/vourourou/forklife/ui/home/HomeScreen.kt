package com.vourourou.forklife.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vourourou.forklife.BuildConfig
import com.vourourou.forklife.R
import com.vourourou.forklife.ui.components.ForkLifeListItem
import com.vourourou.forklife.ui.components.ForkLifeListItemIcon
import com.vourourou.forklife.ui.components.ForkLifeSurfaceCard

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onNavigateToScan: () -> Unit,
    onNavigateToManualScan: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToStats: () -> Unit
) {
    var showAboutDialog by remember { mutableStateOf(false) }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text(stringResource(R.string.about_forklife)) },
            text = {
                Column {
                    Text(stringResource(R.string.version_format, BuildConfig.VERSION_NAME))
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.about_app_description))
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.developed_by),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.data_source),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Quick Actions Section
        item {
            Text(
                text = stringResource(R.string.quick_actions),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            ForkLifeSurfaceCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ForkLifeListItem(
                        headlineText = stringResource(R.string.scan_product),
                        supportingText = stringResource(R.string.scan_product_description),
                        leadingContent = {
                            ForkLifeListItemIcon(
                                icon = Icons.Default.CameraAlt
                            )
                        },
                        onClick = onNavigateToScan
                    )

                    ForkLifeListItem(
                        headlineText = stringResource(R.string.manual_entry),
                        supportingText = stringResource(R.string.manual_entry_description),
                        leadingContent = {
                            ForkLifeListItemIcon(
                                icon = Icons.Default.Edit,
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        },
                        onClick = onNavigateToManualScan
                    )
                }
            }
        }

        // Your Activity Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.your_activity),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            ForkLifeSurfaceCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ForkLifeListItem(
                        headlineText = stringResource(R.string.history),
                        supportingText = stringResource(R.string.history_description),
                        leadingContent = {
                            ForkLifeListItemIcon(
                                icon = Icons.Default.History,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        onClick = onNavigateToHistory
                    )

                    ForkLifeListItem(
                        headlineText = stringResource(R.string.my_stats),
                        supportingText = stringResource(R.string.stats_description),
                        leadingContent = {
                            ForkLifeListItemIcon(
                                icon = Icons.Default.BarChart,
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        },
                        onClick = onNavigateToStats
                    )

                    ForkLifeListItem(
                        headlineText = stringResource(R.string.about),
                        supportingText = stringResource(R.string.about_description),
                        leadingContent = {
                            ForkLifeListItemIcon(
                                icon = Icons.Default.Info,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        onClick = { showAboutDialog = true }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
