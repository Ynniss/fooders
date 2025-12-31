package com.vourourou.forklife.ui.scan.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vourourou.forklife.R
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.ui.theme.ForkLifeCustomShapes

@Composable
fun CharacteristicsTab(product: Product) {
    var showImagePreview by remember { mutableStateOf(false) }

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
                    contentDescription = stringResource(R.string.nutrition_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(ForkLifeCustomShapes.Card)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { showImagePreview = true }
                )
            }
        }

        // Image Preview Dialog
        if (showImagePreview && product.image_nutrition_url != null) {
            ImagePreviewDialog(
                imageUrl = product.image_nutrition_url,
                contentDescription = stringResource(R.string.nutrition_image),
                onDismiss = { showImagePreview = false }
            )
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
                    text = stringResource(R.string.nutritional_values),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                product.nutriments?.let { nutriments ->
                    NutrimentRow(
                        label = stringResource(R.string.energy),
                        value = stringResource(R.string.kcal_format, nutriments.energy_kcal_100g?.toString() ?: stringResource(R.string.not_available))
                    )
                    NutrimentRow(
                        label = stringResource(R.string.fat),
                        value = stringResource(R.string.grams_format, nutriments.fat_100g?.toString() ?: stringResource(R.string.not_available))
                    )
                    NutrimentRow(
                        label = stringResource(R.string.sugars),
                        value = stringResource(R.string.grams_format, nutriments.sugars_100g?.toString() ?: stringResource(R.string.not_available))
                    )
                    NutrimentRow(
                        label = stringResource(R.string.salt),
                        value = stringResource(R.string.grams_format, nutriments.salt_100g?.toString() ?: stringResource(R.string.not_available))
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

@Composable
fun ImagePreviewDialog(
    imageUrl: String,
    contentDescription: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        offset = if (scale > 1f) {
            offset + panChange
        } else {
            Offset.Zero
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .clickable(onClick = onDismiss)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state = transformableState)
                    .clickable { },
                contentScale = ContentScale.Fit
            )

            // Close button
            FilledIconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.6f),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close)
                )
            }
        }
    }
}
