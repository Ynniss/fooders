package com.esgi.fooders.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.esgi.fooders.ui.components.FeatureCard
import com.esgi.fooders.ui.components.FoodersLargeTopAppBar
import com.esgi.fooders.ui.components.GradientType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToScan: () -> Unit,
    onNavigateToManualScan: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FoodersLargeTopAppBar(
                title = "Fooders",
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Actions rapides",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Scan Card
            item {
                HomeFeatureCard(
                    title = "Scanner un produit",
                    subtitle = "Utilisez la camera pour scanner un code-barres",
                    icon = Icons.Default.CameraAlt,
                    gradientType = GradientType.Primary,
                    onClick = onNavigateToScan
                )
            }

            // Manual Scan Card
            item {
                HomeFeatureCard(
                    title = "Saisie manuelle",
                    subtitle = "Entrez le code-barres manuellement",
                    icon = Icons.Default.Edit,
                    gradientType = GradientType.Blue,
                    onClick = onNavigateToManualScan
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Votre activite",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // History Card
            item {
                HomeFeatureCard(
                    title = "Historique",
                    subtitle = "Consultez vos scans precedents",
                    icon = Icons.Default.History,
                    gradientType = GradientType.Green,
                    onClick = onNavigateToHistory
                )
            }

            // Profile Card
            item {
                HomeFeatureCard(
                    title = "Mon profil",
                    subtitle = "Vos statistiques et succes",
                    icon = Icons.Default.Person,
                    gradientType = GradientType.Purple,
                    onClick = onNavigateToProfile
                )
            }

            // About Card
            item {
                HomeFeatureCard(
                    title = "A propos",
                    subtitle = "En savoir plus sur Fooders",
                    icon = Icons.Default.Info,
                    gradientType = GradientType.Orange,
                    onClick = { /* TODO: About dialog */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun HomeFeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradientType: GradientType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FeatureCard(
        title = title,
        subtitle = subtitle,
        gradientType = gradientType,
        onClick = onClick,
        modifier = modifier
    )
}
