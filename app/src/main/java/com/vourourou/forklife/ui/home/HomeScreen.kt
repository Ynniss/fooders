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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vourourou.forklife.ui.components.ForkLifeListItem
import com.vourourou.forklife.ui.components.ForkLifeListItemIcon
import com.vourourou.forklife.ui.components.ForkLifeSurfaceCard

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onNavigateToScan: () -> Unit,
    onNavigateToManualScan: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
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
                text = "Actions rapides",
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
                        headlineText = "Scanner un produit",
                        supportingText = "Utilisez la camera pour scanner un code-barres",
                        leadingContent = {
                            ForkLifeListItemIcon(
                                icon = Icons.Default.CameraAlt
                            )
                        },
                        onClick = onNavigateToScan
                    )

                    ForkLifeListItem(
                        headlineText = "Saisie manuelle",
                        supportingText = "Entrez le code-barres manuellement",
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
                text = "Votre activite",
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
                        headlineText = "Historique",
                        supportingText = "Consultez vos scans precedents",
                        leadingContent = {
                            ForkLifeListItemIcon(
                                icon = Icons.Default.History,
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        },
                        onClick = onNavigateToHistory
                    )

                    ForkLifeListItem(
                        headlineText = "Mon profil",
                        supportingText = "Vos statistiques et succes",
                        leadingContent = {
                            ForkLifeListItemIcon(
                                icon = Icons.Default.Person,
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        },
                        onClick = onNavigateToProfile
                    )

                    ForkLifeListItem(
                        headlineText = "A propos",
                        supportingText = "En savoir plus sur ForkLife",
                        leadingContent = {
                            ForkLifeListItemIcon(
                                icon = Icons.Default.Info,
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        },
                        onClick = { /* TODO: About dialog */ }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
