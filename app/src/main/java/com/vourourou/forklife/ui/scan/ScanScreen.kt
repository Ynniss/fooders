package com.vourourou.forklife.ui.scan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.BottomSheetDefaults
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vourourou.forklife.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
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
    var isFlashOn by remember { mutableStateOf(false) }
    var camera by remember { mutableStateOf<Camera?>(null) }

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
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
                bottomSheetState.partialExpand()
            }
        }
    }

    // Handle sheet dismissal (when user drags down to hide)
    LaunchedEffect(bottomSheetState.currentValue) {
        if (bottomSheetState.currentValue == SheetValue.Hidden && scannedBarcode != null) {
            scannedBarcode = null
            isScanning = true
            viewModel.resetBooleanCheck()
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

    // Dynamic peek height - only show when there's a scanned barcode
    val peekHeight = if (scannedBarcode != null) 350.dp else 0.dp

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = peekHeight,
        sheetDragHandle = {
            if (scannedBarcode != null) {
                // Material 3 drag handle with proper top padding when expanded
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(
                            WindowInsets.statusBars.only(WindowInsetsSides.Top)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                }
            }
        },
        sheetContent = {
            if (scannedBarcode != null) {
                ProductBottomSheet(
                    productEvent = productEvent,
                    barcode = scannedBarcode ?: ""
                )
            }
        },
        sheetContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetShadowElevation = 8.dp,
        sheetTonalElevation = 0.dp
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
                    },
                    onCameraReady = { cam -> camera = cam }
                )

                // Barcode scanning frame overlay
                BarcodeScannerOverlay(
                    modifier = Modifier.fillMaxSize()
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
                        text = stringResource(R.string.camera_permission_required),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.camera_permission_message),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ForkLifePrimaryButton(
                        text = stringResource(R.string.authorize),
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
                        contentDescription = stringResource(R.string.navigate_back)
                    )
                }

                // Flashlight toggle button
                FilledIconButton(
                    onClick = {
                        camera?.let { cam ->
                            if (cam.cameraInfo.hasFlashUnit()) {
                                isFlashOn = !isFlashOn
                                cam.cameraControl.enableTorch(isFlashOn)
                            }
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isFlashOn) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        },
                        contentColor = if (isFlashOn) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                ) {
                    Icon(
                        imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = stringResource(if (isFlashOn) R.string.flash_off else R.string.flash_on)
                    )
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    isScanning: Boolean,
    onBarcodeScanned: (String) -> Unit,
    onCameraReady: (Camera) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    val currentIsScanning by rememberUpdatedState(isScanning)
    val currentOnBarcodeScanned by rememberUpdatedState(onBarcodeScanned)
    val currentOnCameraReady by rememberUpdatedState(onCameraReady)

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
                        val camera = provider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )
                        currentOnCameraReady(camera)
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
    barcode: String
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    val tabTitles = listOf(R.string.tab_score, R.string.tab_characteristics, R.string.tab_ingredients, R.string.tab_environment)
    val tabIcons = listOf(
        Icons.Default.Info,
        Icons.Default.Science,
        Icons.Default.RestaurantMenu,
        Icons.Default.Eco
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
                    tabTitles.forEachIndexed { index, titleRes ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { }, // Non-interactive during loading
                            icon = {
                                Icon(
                                    imageVector = tabIcons[index],
                                    contentDescription = stringResource(titleRes)
                                )
                            },
                            text = {
                                Text(
                                    text = stringResource(titleRes),
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
                    modifier = Modifier.weight(1f),
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
                        tabTitles.forEachIndexed { index, titleRes ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                icon = {
                                    Icon(
                                        imageVector = tabIcons[index],
                                        contentDescription = stringResource(titleRes)
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(titleRes),
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
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        when (page) {
                            0 -> ScoreTab(product = product)
                            1 -> CharacteristicsTab(product = product)
                            2 -> IngredientsTab(product = product)
                            3 -> EnvironmentTab(product = product)
                        }
                    }
                }
            }

            is ProductInfoSharedViewModel.ProductInformationsEvent.Failure -> {
                // Error Header - similar layout to ProductHeader
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Error icon placeholder
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(ForkLifeCustomShapes.Card)
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.product_not_found),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.code_format, barcode),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Error content
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.product_not_in_database),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
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
                        text = stringResource(R.string.scan_barcode),
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
    var showImagePreview by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image - clickable for preview
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
fun ProductImagePreviewDialog(
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

/**
 * Barcode scanner overlay with a transparent scanning area and darkened surroundings
 */
@Composable
fun BarcodeScannerOverlay(
    modifier: Modifier = Modifier
) {
    val overlayColor = Color.Black.copy(alpha = 0.5f)
    val frameColor = MaterialTheme.colorScheme.primary
    val cornerLength = 40f
    val cornerStrokeWidth = 6f
    val cornerRadius = 16f

    Box(
        modifier = modifier
            .drawBehind {
                val scanAreaWidth = size.width * 0.75f
                val scanAreaHeight = scanAreaWidth * 0.5f // Barcode aspect ratio
                val scanAreaLeft = (size.width - scanAreaWidth) / 2
                val scanAreaTop = (size.height - scanAreaHeight) / 2 - 60.dp.toPx() // Slightly above center

                // Create the scanning area path with rounded corners
                val scanAreaPath = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(scanAreaLeft, scanAreaTop),
                                size = Size(scanAreaWidth, scanAreaHeight)
                            ),
                            cornerRadius = CornerRadius(cornerRadius.dp.toPx())
                        )
                    )
                }

                // Draw darkened overlay with transparent scanning area
                clipPath(scanAreaPath, clipOp = ClipOp.Difference) {
                    drawRect(overlayColor)
                }

                // Draw corner brackets
                val left = scanAreaLeft
                val top = scanAreaTop
                val right = scanAreaLeft + scanAreaWidth
                val bottom = scanAreaTop + scanAreaHeight
                val radius = cornerRadius.dp.toPx()

                // Top-left corner
                drawLine(
                    color = frameColor,
                    start = Offset(left, top + radius + cornerLength),
                    end = Offset(left, top + radius),
                    strokeWidth = cornerStrokeWidth.dp.toPx()
                )
                drawArc(
                    color = frameColor,
                    startAngle = 180f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(left, top),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = cornerStrokeWidth.dp.toPx())
                )
                drawLine(
                    color = frameColor,
                    start = Offset(left + radius, top),
                    end = Offset(left + radius + cornerLength, top),
                    strokeWidth = cornerStrokeWidth.dp.toPx()
                )

                // Top-right corner
                drawLine(
                    color = frameColor,
                    start = Offset(right - radius - cornerLength, top),
                    end = Offset(right - radius, top),
                    strokeWidth = cornerStrokeWidth.dp.toPx()
                )
                drawArc(
                    color = frameColor,
                    startAngle = 270f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(right - radius * 2, top),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = cornerStrokeWidth.dp.toPx())
                )
                drawLine(
                    color = frameColor,
                    start = Offset(right, top + radius),
                    end = Offset(right, top + radius + cornerLength),
                    strokeWidth = cornerStrokeWidth.dp.toPx()
                )

                // Bottom-left corner
                drawLine(
                    color = frameColor,
                    start = Offset(left, bottom - radius - cornerLength),
                    end = Offset(left, bottom - radius),
                    strokeWidth = cornerStrokeWidth.dp.toPx()
                )
                drawArc(
                    color = frameColor,
                    startAngle = 90f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(left, bottom - radius * 2),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = cornerStrokeWidth.dp.toPx())
                )
                drawLine(
                    color = frameColor,
                    start = Offset(left + radius, bottom),
                    end = Offset(left + radius + cornerLength, bottom),
                    strokeWidth = cornerStrokeWidth.dp.toPx()
                )

                // Bottom-right corner
                drawLine(
                    color = frameColor,
                    start = Offset(right - radius - cornerLength, bottom),
                    end = Offset(right - radius, bottom),
                    strokeWidth = cornerStrokeWidth.dp.toPx()
                )
                drawArc(
                    color = frameColor,
                    startAngle = 0f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(right - radius * 2, bottom - radius * 2),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = cornerStrokeWidth.dp.toPx())
                )
                drawLine(
                    color = frameColor,
                    start = Offset(right, bottom - radius),
                    end = Offset(right, bottom - radius - cornerLength),
                    strokeWidth = cornerStrokeWidth.dp.toPx()
                )
            }
    ) {
        // Hint text below the scanning area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 180.dp)
            ) {
                Text(
                    text = stringResource(R.string.place_barcode_in_frame),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
