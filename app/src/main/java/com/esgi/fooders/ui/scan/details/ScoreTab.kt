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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.ui.theme.FoodersCustomShapes

@Composable
fun ScoreTab(product: ProductInformationsResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nutriscore
        ScoreCard(
            title = "Nutri-Score",
            grade = product.data?.nutriscore_grade?.uppercase() ?: "?",
            description = "Qualite nutritionnelle"
        )

        // Ecoscore
        ScoreCard(
            title = "Eco-Score",
            grade = product.data?.ecoscore_grade?.uppercase() ?: "?",
            description = "Impact environnemental"
        )

        // NOVA Group
        ScoreCard(
            title = "NOVA",
            grade = product.data?.nova_group?.toString() ?: "?",
            description = "Niveau de transformation"
        )
    }
}

@Composable
fun ScoreCard(
    title: String,
    grade: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = FoodersCustomShapes.Card,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Grade Badge
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(getGradeColor(grade)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = grade,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun getGradeColor(grade: String): Color {
    return when (grade.uppercase()) {
        "A", "1" -> Color(0xFF038141) // Green
        "B", "2" -> Color(0xFF85BB2F) // Light Green
        "C", "3" -> Color(0xFFFECB02) // Yellow
        "D", "4" -> Color(0xFFEE8100) // Orange
        "E" -> Color(0xFFE63E11) // Red
        else -> Color.Gray
    }
}
