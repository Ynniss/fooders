package com.vourourou.forklife.ui.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vourourou.forklife.ui.components.SkeletonImageSquare
import com.vourourou.forklife.ui.components.SkeletonText
import com.vourourou.forklife.ui.components.SkeletonTextShort
import com.vourourou.forklife.ui.theme.ForkLifeCustomShapes

@Composable
fun ProductHeaderSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image Skeleton
        SkeletonImageSquare(
            size = 80.dp,
            shape = ForkLifeCustomShapes.Card
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Product Name Skeleton (2 lines)
            SkeletonText(
                style = MaterialTheme.typography.titleLarge,
                widthFraction = 0.9f
            )
            Spacer(modifier = Modifier.padding(2.dp))
            SkeletonText(
                style = MaterialTheme.typography.titleLarge,
                widthFraction = 0.6f
            )
            Spacer(modifier = Modifier.padding(4.dp))
            // Product Code Skeleton
            SkeletonTextShort(
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
