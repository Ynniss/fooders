package com.vourourou.forklife.ui.scan.details.skeletons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vourourou.forklife.ui.components.ForkLifeSurfaceCard
import com.vourourou.forklife.ui.components.SkeletonBox
import com.vourourou.forklife.ui.components.SkeletonText
import com.vourourou.forklife.ui.components.SkeletonTextLong
import com.vourourou.forklife.ui.components.SkeletonTextMedium

@Composable
fun EnvironmentTabSkeleton() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Eco-Score Card
        item {
            EcoScoreCardSkeleton()
        }

        // Packaging Card
        item {
            PackagingCardSkeleton()
        }
    }
}

@Composable
private fun EcoScoreCardSkeleton() {
    ForkLifeSurfaceCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular icon
            SkeletonBox(
                width = 80.dp,
                height = 80.dp,
                shape = CircleShape
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Title
            SkeletonTextMedium(
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Grade text
            SkeletonText(
                width = 100.dp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun PackagingCardSkeleton() {
    ForkLifeSurfaceCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon
            SkeletonBox(
                width = 40.dp,
                height = 40.dp,
                shape = CircleShape
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title
                SkeletonTextMedium(
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Packaging text (2 lines)
                SkeletonTextLong(
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                SkeletonTextMedium(
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
