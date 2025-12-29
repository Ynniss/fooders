package com.vourourou.forklife.ui.scan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.ui.components.ForkLifeOutlinedButton
import com.vourourou.forklife.ui.components.ForkLifePrimaryButton
import com.vourourou.forklife.ui.components.LoadingIndicator
import com.vourourou.forklife.ui.scan.details.CharacteristicsTab
import com.vourourou.forklife.ui.scan.details.EnvironmentTab
import com.vourourou.forklife.ui.scan.details.IngredientsTab
import com.vourourou.forklife.ui.scan.details.ScoreTab
import com.vourourou.forklife.ui.scan.details.skeletons.CharacteristicsTabSkeleton
import com.vourourou.forklife.ui.scan.details.skeletons.EnvironmentTabSkeleton
import com.vourourou.forklife.ui.scan.details.skeletons.IngredientsTabSkeleton
import com.vourourou.forklife.ui.scan.details.skeletons.ScoreTabSkeleton
import com.vourourou.forklife.ui.theme.ForkLifeCustomShapes
import com.vourourou.forklife.utils.BarcodeAnalyzer
import com.vourourou.forklife.utils.Resource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    onNavigateBack: () -> Unit,
    onNavigateToManualScan: () -> Unit,
    viewModel: ProductInfoSharedViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val productEvent by viewModel.productInformationsEvent.observeAsState(
        ProductInfoSharedViewModel.ProductInformationsEvent.Empty
    )

    var scannedBarcode by remember { mutableStateOf<String?>(null) }
    var isScanning by remember { mutableStateOf(true) }

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(scannedBarcode) {
        scannedBarcode?.let { barcode ->
            viewModel.getProductInformations(barcode)
            scope.launch {
                bottomSheetState.expand()
            }
        }
    }

    // Track successful scans
    LaunchedEffect(productEvent) {
        val event = productEvent
        if (event is ProductInfoSharedViewModel.ProductInformationsEvent.Success) {
            if (event.result.data != null) {
                context.findActivity()?.let { activity ->
                    viewModel.trackScan(
                        activity = activity,
                        productData = event.result.data
                    )
                }
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            ProductBottomSheet(
                productEvent = productEvent,
                barcode = scannedBarcode ?: "",
                onDismiss = {
                    scope.launch {
                        bottomSheetState.hide()
                        isScanning = true
                        scannedBarcode = null
                        viewModel.resetBooleanCheck()
                    }
                }
            )
        },
        sheetContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        sheetShape = ForkLifeCustomShapes.BottomSheet
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cameraPermissionState.status.isGranted) {
                // Camera Preview
                CameraPreview(
                    isScanning = isScanning,
                    onBarcodeScanned = { barcode ->
                        if (isScanning && scannedBarcode == null) {
                            isScanning = false
                            scannedBarcode = barcode
                        }
                    }
                )
            } else {
                // Permission not granted
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Permission camera requise",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Veuillez autoriser l'acces a la camera pour scanner des codes-barres",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ForkLifePrimaryButton(
                        text = "Autoriser",
                        onClick = { cameraPermissionState.launchPermissionRequest() }
                    )
                }
            }

            // Top bar overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    onClick = onNavigateBack,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour"
                    )
                }

                FilledIconButton(
                    onClick = onNavigateToManualScan,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Dialpad,
                        contentDescription = "Saisie manuelle"
                    )
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    isScanning: Boolean,
    onBarcodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    val currentIsScanning by rememberUpdatedState(isScanning)
    val currentOnBarcodeScanned by rememberUpdatedState(onBarcodeScanned)

    DisposableEffect(lifecycleOwner) {
        onDispose {
            try {
                cameraProvider?.unbindAll()
            } catch (e: Exception) {
                Log.e("CameraPreview", "Error unbinding camera", e)
            }
            cameraExecutor.shutdown()
        }
    }

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }.also { previewView ->
                cameraProviderFuture.addListener({
                    try {
                        val provider = cameraProviderFuture.get()
                        cameraProvider = provider

                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                                    if (currentIsScanning) {
                                        currentOnBarcodeScanned(barcode)
                                    }
                                })
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )
                        Log.d("CameraPreview", "Camera bound successfully")
                    } catch (e: Exception) {
                        Log.e("CameraPreview", "Camera initialization failed", e)
                    }
                }, ContextCompat.getMainExecutor(ctx))
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductBottomSheet(
    productEvent: ProductInfoSharedViewModel.ProductInformationsEvent,
    barcode: String,
    onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    val tabTitles = listOf("Score", "Caracteristiques", "Ingredients", "Environnement")
    val tabIcons = listOf(
        Icons.Default.Info,
        Icons.Default.Science,
        Icons.Default.RestaurantMenu,
        Icons.Default.Eco
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        when (productEvent) {
            is ProductInfoSharedViewModel.ProductInformationsEvent.Loading -> {
                // Product Header Skeleton
                ProductHeaderSkeleton()

                // Tabs (visible but non-interactive during loading)
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { }, // Non-interactive during loading
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

                // Pager with skeleton content
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.height(350.dp),
                    userScrollEnabled = false // Disable scrolling during loading
                ) { page ->
                    when (page) {
                        0 -> ScoreTabSkeleton()
                        1 -> CharacteristicsTabSkeleton()
                        2 -> IngredientsTabSkeleton()
                        3 -> EnvironmentTabSkeleton()
                    }
                }
            }

            is ProductInfoSharedViewModel.ProductInformationsEvent.Success -> {
                val product = productEvent.result.data

                if (product != null) {
                    // Product Header
                    ProductHeader(product = product)

                    // Tabs
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
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
                        modifier = Modifier.height(350.dp)
                    ) { page ->
                        when (page) {
                            0 -> ScoreTab(product = product)
                            1 -> CharacteristicsTab(product = product)
                            2 -> IngredientsTab(product = product)
                            3 -> EnvironmentTab(product = product)
                        }
                    }

                    // Bottom actions
                    ForkLifePrimaryButton(
                        text = "Fermer",
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            is ProductInfoSharedViewModel.ProductInformationsEvent.Failure -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Produit non trouve",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Code-barres: $barcode",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ce produit n'est pas dans la base de donnees",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ForkLifePrimaryButton(
                        text = "Fermer",
                        onClick = onDismiss
                    )
                }
            }

            else -> {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Scannez un code-barres",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ProductHeader(
    product: Product
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        AsyncImage(
            model = product.image_front_url,
            contentDescription = "Product image",
            modifier = Modifier
                .size(80.dp)
                .clip(ForkLifeCustomShapes.Card)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.product_name ?: "Produit inconnu",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Code: ${product.code}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Helper function to safely get Activity from Context
 */
private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
