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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vourourou.forklife.R
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.ui.theme.ForkLifeCustomShapes

// Valid grades for Nutri-Score and Eco-Score
private val validLetterGrades = listOf("A", "B", "C", "D", "E")
// Valid NOVA groups
private val validNovaGroups = listOf(1, 2, 3, 4)

@Composable
fun ScoreTab(product: Product) {
    // Check if grades are valid (not just non-null)
    val hasValidNutriscore = product.nutriscore_grade?.uppercase() in validLetterGrades
    val hasValidEcoscore = product.ecoscore_grade?.uppercase() in validLetterGrades
    val hasValidNova = product.nova_group in validNovaGroups

    val hasAnyValidScore = hasValidNutriscore || hasValidEcoscore || hasValidNova

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (hasAnyValidScore) {
            // Nutriscore - only show if valid grade (A-E)
            if (hasValidNutriscore) {
                ScoreCard(
                    title = stringResource(R.string.nutri_score),
                    grade = product.nutriscore_grade!!.uppercase(),
                    description = stringResource(R.string.nutritional_quality)
                )
            }

            // Ecoscore - only show if valid grade (A-E)
            if (hasValidEcoscore) {
                ScoreCard(
                    title = stringResource(R.string.eco_score),
                    grade = product.ecoscore_grade!!.uppercase(),
                    description = stringResource(R.string.environmental_impact)
                )
            }

            // NOVA Group - only show if valid (1-4)
            if (hasValidNova) {
                ScoreCard(
                    title = stringResource(R.string.nova),
                    grade = product.nova_group.toString(),
                    description = stringResource(R.string.processing_level)
                )
            }
        } else {
            // No valid scores available
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_scores_available),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ScoreCard(
    title: String,
    grade: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val isKnown = isKnownGrade(grade)
    val textColor = if (isKnown) getGradeTextColor(grade) else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = ForkLifeCustomShapes.Card,
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
                    color = textColor
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
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}

@Composable
private fun isKnownGrade(grade: String): Boolean {
    return grade.uppercase() in listOf("A", "B", "C", "D", "E", "1", "2", "3", "4")
}

@Composable
private fun getGradeTextColor(grade: String): Color {
    // Use black text on light backgrounds (B, C grades) for better contrast
    return when (grade.uppercase()) {
        "B", "C", "2", "3" -> Color.Black
        else -> Color.White
    }
}
