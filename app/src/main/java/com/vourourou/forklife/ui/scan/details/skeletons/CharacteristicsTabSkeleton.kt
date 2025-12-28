package com.vourourou.forklife.ui.scan.details.skeletons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vourourou.forklife.ui.components.ForkLifeSurfaceCard
import com.vourourou.forklife.ui.components.SkeletonImageFillWidth
import com.vourourou.forklife.ui.components.SkeletonText
import com.vourourou.forklife.ui.components.SkeletonTextMedium
import com.vourourou.forklife.ui.components.SkeletonTextShort

@Composable
fun CharacteristicsTabSkeleton() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Nutrition Image
        item {
            SkeletonImageFillWidth(
                height = 150.dp,
                shape = MaterialTheme.shapes.medium
            )
        }

        // Nutriments Card
        item {
            ForkLifeSurfaceCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Title
                    SkeletonTextMedium(
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // 4 Nutriment Rows
                    repeat(4) { index ->
                        if (index > 0) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                        NutrimentRowSkeleton()
                    }
                }
            }
        }
    }
}

@Composable
private fun NutrimentRowSkeleton() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Label
        SkeletonText(
            style = MaterialTheme.typography.bodyMedium,
            widthFraction = 0.4f
        )
        Spacer(modifier = Modifier.weight(1f))
        // Value
        SkeletonTextShort(
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
