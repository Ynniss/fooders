package com.vourourou.forklife.ui.scan.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.ui.theme.ForkLifeCustomShapes

@Composable
fun CharacteristicsTab(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nutrition Image
        product.image_nutrition_url?.let { url ->
            if (url.isNotEmpty()) {
                AsyncImage(
                    model = url,
                    contentDescription = "Nutrition image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(ForkLifeCustomShapes.Card)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }

        // Nutriments Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = ForkLifeCustomShapes.Card,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Valeurs nutritionnelles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                product.nutriments?.let { nutriments ->
                    NutrimentRow(
                        label = "Energie",
                        value = "${nutriments.energy_kcal_100g ?: "N/A"} kcal"
                    )
                    NutrimentRow(
                        label = "Matieres grasses",
                        value = "${nutriments.fat_100g ?: "N/A"} g"
                    )
                    NutrimentRow(
                        label = "Sucres",
                        value = "${nutriments.sugars_100g ?: "N/A"} g"
                    )
                    NutrimentRow(
                        label = "Sel",
                        value = "${nutriments.salt_100g ?: "N/A"} g"
                    )
                }
            }
        }
    }
}

@Composable
fun NutrimentRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
