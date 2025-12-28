package com.vourourou.forklife.ui.scan.details.skeletons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vourourou.forklife.ui.components.ForkLifeSurfaceCard
import com.vourourou.forklife.ui.components.SkeletonBox
import com.vourourou.forklife.ui.components.SkeletonText
import com.vourourou.forklife.ui.components.SkeletonTextMedium

@Composable
fun ScoreTabSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Three score cards
        repeat(3) {
            ScoreCardSkeleton()
        }
    }
}

@Composable
private fun ScoreCardSkeleton() {
    ForkLifeSurfaceCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular grade badge
            SkeletonBox(
                width = 56.dp,
                height = 56.dp,
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
                Spacer(modifier = Modifier.height(4.dp))
                // Description
                SkeletonText(
                    style = MaterialTheme.typography.bodyMedium,
                    widthFraction = 0.8f
                )
            }
        }
    }
}
