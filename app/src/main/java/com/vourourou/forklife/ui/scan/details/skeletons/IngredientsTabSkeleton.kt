package com.vourourou.forklife.ui.scan.details.skeletons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vourourou.forklife.ui.components.ForkLifeSurfaceCard
import com.vourourou.forklife.ui.components.SkeletonImageFillWidth
import com.vourourou.forklife.ui.components.SkeletonText
import com.vourourou.forklife.ui.components.SkeletonTextLong
import com.vourourou.forklife.ui.components.SkeletonTextMedium

@Composable
fun IngredientsTabSkeleton() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Ingredients Image
        item {
            SkeletonImageFillWidth(
                height = 150.dp,
                shape = MaterialTheme.shapes.medium
            )
        }

        // Ingredients Card
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

                    // Ingredients Text (3-4 lines)
                    SkeletonText(
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SkeletonTextLong(
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SkeletonText(
                        style = MaterialTheme.typography.bodyMedium,
                        widthFraction = 0.85f
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SkeletonTextMedium(
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
