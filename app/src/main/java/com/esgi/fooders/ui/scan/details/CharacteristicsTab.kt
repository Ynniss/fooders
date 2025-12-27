package com.esgi.fooders.ui.scan.details

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
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.ui.theme.FoodersCustomShapes

@Composable
fun CharacteristicsTab(product: ProductInformationsResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nutrition Image
        product.data?.image_nutrition_url?.let { url ->
            if (url.isNotEmpty()) {
                AsyncImage(
                    model = url,
                    contentDescription = "Nutrition image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(FoodersCustomShapes.Card)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }

        // Nutriments Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = FoodersCustomShapes.Card,
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

                product.data?.nutriments?.let { nutriments ->
                    NutrimentRow(
                        label = "Energie",
                        value = "${nutriments.energy_value ?: "N/A"} ${nutriments.energy_unit ?: "kJ"}"
                    )
                    NutrimentRow(
                        label = "Matieres grasses",
                        value = "${nutriments.fat ?: "N/A"} g"
                    )
                    NutrimentRow(
                        label = "Sucres",
                        value = "${nutriments.sugars ?: "N/A"} g"
                    )
                    NutrimentRow(
                        label = "Sel",
                        value = "${nutriments.salt ?: "N/A"} g"
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
