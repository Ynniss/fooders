package com.esgi.fooders.ui.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.fooders.ui.components.FoodersBackTopAppBar
import com.esgi.fooders.ui.components.FoodersOutlinedButton
import com.esgi.fooders.ui.components.FoodersPrimaryButton
import com.esgi.fooders.ui.components.FoodersTextField
import com.esgi.fooders.ui.components.LoadingIndicator
import com.esgi.fooders.ui.scan.details.CharacteristicsTab
import com.esgi.fooders.ui.scan.details.EnvironmentTab
import com.esgi.fooders.ui.scan.details.IngredientsTab
import com.esgi.fooders.ui.scan.details.ScoreTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualScanScreen(
    initialBarcode: String,
    onNavigateBack: () -> Unit,
    onNavigateToEditProduct: (String, String) -> Unit,
    viewModel: ProductInfoSharedViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()

    var barcode by remember { mutableStateOf(initialBarcode) }
    val productEvent by viewModel.productInformationsEvent.observeAsState(
        ProductInfoSharedViewModel.ProductInformationsEvent.Empty
    )

    val pagerState = rememberPagerState(pageCount = { 4 })
    val tabTitles = listOf("Score", "Caracteristiques", "Ingredients", "Environnement")
    val tabIcons = listOf(
        Icons.Default.Info,
        Icons.Default.Science,
        Icons.Default.RestaurantMenu,
        Icons.Default.Eco
    )

    LaunchedEffect(initialBarcode) {
        if (initialBarcode.isNotEmpty()) {
            viewModel.getProductInformations(initialBarcode)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FoodersBackTopAppBar(
                title = "Saisie manuelle",
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                FoodersTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    label = "Code-barres",
                    leadingIcon = Icons.Default.QrCodeScanner,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                FoodersPrimaryButton(
                    text = "Rechercher",
                    onClick = {
                        if (barcode.isNotBlank()) {
                            viewModel.getProductInformations(barcode)
                        }
                    },
                    icon = Icons.Default.Search,
                    enabled = barcode.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Product Content
            when (productEvent) {
                is ProductInfoSharedViewModel.ProductInformationsEvent.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }

                is ProductInfoSharedViewModel.ProductInformationsEvent.Success -> {
                    val product = (productEvent as ProductInfoSharedViewModel.ProductInformationsEvent.Success).result.data

                    if (product != null) {
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
                            modifier = Modifier.weight(1f)
                        ) { page ->
                            when (page) {
                                0 -> ScoreTab(product = product)
                                1 -> CharacteristicsTab(product = product)
                                2 -> IngredientsTab(product = product)
                                3 -> EnvironmentTab(product = product)
                            }
                        }

                        // Bottom Actions
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .navigationBarsPadding(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            FoodersOutlinedButton(
                                text = "Nouveau scan",
                                onClick = {
                                    barcode = ""
                                    viewModel.resetBooleanCheck()
                                },
                                modifier = Modifier.weight(1f)
                            )
                            FoodersPrimaryButton(
                                text = "Modifier",
                                onClick = { onNavigateToEditProduct(barcode, "info") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                is ProductInfoSharedViewModel.ProductInformationsEvent.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
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
                            Spacer(modifier = Modifier.height(24.dp))
                            FoodersPrimaryButton(
                                text = "Ajouter ce produit",
                                onClick = { onNavigateToEditProduct(barcode, "new") }
                            )
                        }
                    }
                }

                else -> {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Entrez un code-barres pour rechercher",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
