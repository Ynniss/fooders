package com.vourourou.forklife.ui.scan.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vourourou.forklife.R
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.ui.theme.ForkLifeCustomShapes

@Composable
fun IngredientsTab(product: Product) {
    var showImagePreview by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Ingredients Image
        product.image_ingredients_url?.let { url ->
            if (url.isNotEmpty()) {
                AsyncImage(
                    model = url,
                    contentDescription = stringResource(R.string.ingredients_image),
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
        if (showImagePreview && product.image_ingredients_url != null) {
            ImagePreviewDialog(
                imageUrl = product.image_ingredients_url,
                contentDescription = stringResource(R.string.ingredients_image),
                onDismiss = { showImagePreview = false }
            )
        }

        // Ingredients Text
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = ForkLifeCustomShapes.Card,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.ingredients_list),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = product.ingredients_text?.takeIf { it.isNotEmpty() }
                        ?: stringResource(R.string.no_ingredients_available),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
