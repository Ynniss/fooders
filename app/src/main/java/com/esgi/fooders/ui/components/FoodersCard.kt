package com.esgi.fooders.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.esgi.fooders.ui.theme.FoodersCustomShapes
import com.esgi.fooders.ui.theme.FoodersTheme
import com.esgi.fooders.ui.theme.GradientColors

@Composable
fun FoodersCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Card(
        modifier = cardModifier,
        shape = FoodersCustomShapes.Card,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(content = content)
    }
}

@Composable
fun FoodersElevatedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    elevation: Dp = 4.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        ElevatedCard(
            onClick = onClick,
            modifier = modifier,
            shape = FoodersCustomShapes.Card,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = elevation
            )
        ) {
            Column(content = content)
        }
    } else {
        ElevatedCard(
            modifier = modifier,
            shape = FoodersCustomShapes.Card,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = elevation
            )
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun FoodersOutlinedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        OutlinedCard(
            onClick = onClick,
            modifier = modifier,
            shape = FoodersCustomShapes.Card,
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(content = content)
        }
    } else {
        OutlinedCard(
            modifier = modifier,
            shape = FoodersCustomShapes.Card,
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(content = content)
        }
    }
}

enum class GradientType {
    Primary,
    Orange,
    Green,
    Blue,
    Purple,
    Red
}

@Composable
fun FoodersGradientCard(
    modifier: Modifier = Modifier,
    gradientType: GradientType = GradientType.Primary,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val gradient = when (gradientType) {
        GradientType.Primary -> Brush.horizontalGradient(
            colors = listOf(
                FoodersTheme.colors.cardGradientStart,
                FoodersTheme.colors.cardGradientEnd
            )
        )
        GradientType.Orange -> Brush.horizontalGradient(
            colors = listOf(GradientColors.OrangeStart, GradientColors.OrangeEnd)
        )
        GradientType.Green -> Brush.horizontalGradient(
            colors = listOf(GradientColors.GreenStart, GradientColors.GreenEnd)
        )
        GradientType.Blue -> Brush.horizontalGradient(
            colors = listOf(GradientColors.BlueStart, GradientColors.BlueEnd)
        )
        GradientType.Purple -> Brush.horizontalGradient(
            colors = listOf(GradientColors.PurpleStart, GradientColors.PurpleEnd)
        )
        GradientType.Red -> Brush.horizontalGradient(
            colors = listOf(GradientColors.RedStart, GradientColors.RedEnd)
        )
    }

    val cardModifier = modifier
        .clip(FoodersCustomShapes.Card)
        .background(gradient)
        .then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )

    Box(modifier = cardModifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun FeatureCard(
    title: String,
    subtitle: String,
    gradientType: GradientType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FoodersGradientCard(
        modifier = modifier.fillMaxWidth(),
        gradientType = gradientType,
        onClick = onClick
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
