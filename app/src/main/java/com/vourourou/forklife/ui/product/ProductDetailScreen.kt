package com.vourourou.forklife.ui.product

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.vourourou.forklife.R
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.ui.components.LoadingIndicator
import com.vourourou.forklife.ui.scan.ProductInfoSharedViewModel
import com.vourourou.forklife.ui.scan.details.CharacteristicsTab
import com.vourourou.forklife.ui.scan.details.EnvironmentTab
import com.vourourou.forklife.ui.scan.details.IngredientsTab
import com.vourourou.forklife.ui.scan.details.ScoreTab
import com.vourourou.forklife.ui.scan.details.skeletons.CharacteristicsTabSkeleton
import com.vourourou.forklife.ui.scan.details.skeletons.EnvironmentTabSkeleton
import com.vourourou.forklife.ui.scan.details.skeletons.IngredientsTabSkeleton
import com.vourourou.forklife.ui.scan.details.skeletons.ScoreTabSkeleton
import com.vourourou.forklife.ui.theme.ForkLifeCustomShapes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    barcode: String,
    onNavigateBack: () -> Unit,
    viewModel: ProductInfoSharedViewModel = hiltViewModel()
) {
    val productEvent by viewModel.productInformationsEvent.observeAsState(
        ProductInfoSharedViewModel.ProductInformationsEvent.Empty
    )

    LaunchedEffect(barcode) {
        if (barcode.isNotEmpty()) {
            viewModel.getProductInformations(barcode)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.product_details)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (productEvent) {
                is ProductInfoSharedViewModel.ProductInformationsEvent.Loading -> {
                    ProductDetailContent(
                        product = null,
                        isLoading = true
                    )
                }

                is ProductInfoSharedViewModel.ProductInformationsEvent.Success -> {
                    val product = (productEvent as ProductInfoSharedViewModel.ProductInformationsEvent.Success).result.data
                    if (product != null) {
                        ProductDetailContent(
                            product = product,
                            isLoading = false
                        )
                    }
                }

                is ProductInfoSharedViewModel.ProductInformationsEvent.Failure -> {
                    ProductNotFoundContent(barcode = barcode)
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductDetailContent(
    product: Product?,
    isLoading: Boolean
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    val tabTitles = listOf(
        stringResource(R.string.tab_score),
        stringResource(R.string.tab_characteristics),
        stringResource(R.string.tab_ingredients),
        stringResource(R.string.tab_environment)
    )
    val tabIcons = listOf(
        Icons.Default.Info,
        Icons.Default.Science,
        Icons.Default.RestaurantMenu,
        Icons.Default.Eco
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Product Header
        if (isLoading) {
            ProductHeaderSkeleton()
        } else if (product != null) {
            ProductHeader(product = product)
        }

        // Tabs
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        if (!isLoading) {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = tabIcons[index],
                            contentDescription = title
                        )
                    },
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = !isLoading
        ) { page ->
            if (isLoading) {
                when (page) {
                    0 -> ScoreTabSkeleton()
                    1 -> CharacteristicsTabSkeleton()
                    2 -> IngredientsTabSkeleton()
                    3 -> EnvironmentTabSkeleton()
                }
            } else if (product != null) {
                when (page) {
                    0 -> ScoreTab(product = product)
                    1 -> CharacteristicsTab(product = product)
                    2 -> IngredientsTab(product = product)
                    3 -> EnvironmentTab(product = product)
                }
            }
        }
    }
}

@Composable
private fun ProductHeader(product: Product) {
    var showImagePreview by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.image_front_url,
            contentDescription = stringResource(R.string.product_image),
            modifier = Modifier
                .size(80.dp)
                .clip(ForkLifeCustomShapes.Card)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(enabled = !product.image_front_url.isNullOrEmpty()) {
                    showImagePreview = true
                }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.product_name ?: stringResource(R.string.unknown_product),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(R.string.code_format, product.code),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    // Image Preview Dialog
    if (showImagePreview && !product.image_front_url.isNullOrEmpty()) {
        ProductImagePreviewDialog(
            imageUrl = product.image_front_url!!,
            productName = product.product_name ?: stringResource(R.string.unknown_product),
            onDismiss = { showImagePreview = false }
        )
    }
}

@Composable
private fun ProductImagePreviewDialog(
    imageUrl: String,
    productName: String,
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
                contentDescription = productName,
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

@Composable
private fun ProductHeaderSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(ForkLifeCustomShapes.Card)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(24.dp)
                    .clip(ForkLifeCustomShapes.Card)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
                    .clip(ForkLifeCustomShapes.Card)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}

@Composable
private fun ProductNotFoundContent(barcode: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.product_not_found),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.barcode_format, barcode),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.product_not_in_database),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
