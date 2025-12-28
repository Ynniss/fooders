package com.vourourou.forklife.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vourourou.forklife.ui.theme.AvocadoPrimary
import com.vourourou.forklife.ui.theme.CherryPrimary
import com.vourourou.forklife.ui.theme.ForkLifeCustomShapes
import com.vourourou.forklife.ui.theme.OrangePrimary
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val currentTheme by viewModel.currentTheme.collectAsState()
    val currentDarkMode by viewModel.currentDarkMode.collectAsState()
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Theme Color Section
        item {
            SettingsSection(title = "Couleur du theme") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DarkModeOption(
                        name = "Dynamique",
                        description = "Couleurs de votre fond d'ecran (Android 12+)",
                        icon = Icons.Default.Wallpaper,
                        isSelected = currentTheme == "Dynamic",
                        onClick = {
                            scope.launch { viewModel.updateTheme("Dynamic") }
                        }
                    )
                    ThemeColorOption(
                        name = "Orange",
                        color = OrangePrimary,
                        isSelected = currentTheme == "Orange",
                        onClick = {
                            scope.launch { viewModel.updateTheme("Orange") }
                        }
                    )
                    ThemeColorOption(
                        name = "Avocado",
                        color = AvocadoPrimary,
                        isSelected = currentTheme == "Avocado",
                        onClick = {
                            scope.launch { viewModel.updateTheme("Avocado") }
                        }
                    )
                    ThemeColorOption(
                        name = "Cherry",
                        color = CherryPrimary,
                        isSelected = currentTheme == "Cherry",
                        onClick = {
                            scope.launch { viewModel.updateTheme("Cherry") }
                        }
                    )
                }
            }
        }

        // Dark Mode Section
        item {
            SettingsSection(title = "Mode sombre") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DarkModeOption(
                        name = "Systeme",
                        description = "Suivre les parametres du systeme",
                        icon = Icons.Default.PhoneAndroid,
                        isSelected = currentDarkMode == "System",
                        onClick = {
                            scope.launch { viewModel.updateDarkMode("System") }
                        }
                    )
                    DarkModeOption(
                        name = "Clair",
                        description = "Toujours utiliser le mode clair",
                        icon = Icons.Default.LightMode,
                        isSelected = currentDarkMode == "Light",
                        onClick = {
                            scope.launch { viewModel.updateDarkMode("Light") }
                        }
                    )
                    DarkModeOption(
                        name = "Sombre",
                        description = "Toujours utiliser le mode sombre",
                        icon = Icons.Default.DarkMode,
                        isSelected = currentDarkMode == "Dark",
                        onClick = {
                            scope.launch { viewModel.updateDarkMode("Dark") }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = ForkLifeCustomShapes.Card,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ThemeColorOption(
    name: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ForkLifeCustomShapes.CardSmall)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }
}

@Composable
fun DarkModeOption(
    name: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ForkLifeCustomShapes.CardSmall)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }
}
