package com.esgi.fooders.ui.editproduct

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.fooders.ui.components.FoodersBackTopAppBar
import com.esgi.fooders.ui.components.FoodersFullWidthButton
import com.esgi.fooders.ui.components.FoodersTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    barcode: String,
    type: String,
    onNavigateBack: () -> Unit,
    onProductUpdated: () -> Unit,
    viewModel: EditProductViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var productName by remember { mutableStateOf("") }
    var packaging by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }

    val modificationResult by viewModel.modificationEvent.observeAsState()

    LaunchedEffect(modificationResult) {
        modificationResult?.let { result ->
            if (result.contains("ok", ignoreCase = true)) {
                Toast.makeText(context, "Produit modifie avec succes", Toast.LENGTH_SHORT).show()
                onProductUpdated()
            } else if (result.contains("error", ignoreCase = true)) {
                Toast.makeText(context, "Erreur lors de la modification", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FoodersBackTopAppBar(
                title = if (type == "new") "Ajouter un produit" else "Modifier le produit",
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            FoodersTextField(
                value = productName,
                onValueChange = { productName = it },
                label = "Nom du produit",
                leadingIcon = Icons.Default.DriveFileRenameOutline,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            FoodersTextField(
                value = packaging,
                onValueChange = { packaging = it },
                label = "Emballage",
                leadingIcon = Icons.Default.Inventory2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            FoodersTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = "Ingredients",
                leadingIcon = Icons.Default.RestaurantMenu,
                singleLine = false,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            FoodersFullWidthButton(
                text = "Enregistrer les modifications",
                onClick = {
                    viewModel.modifyProductInfo(
                        code = barcode,
                        productName = productName,
                        packaging = packaging,
                        ingredientsText = ingredients
                    )
                },
                enabled = productName.isNotBlank() || packaging.isNotBlank() || ingredients.isNotBlank()
            )
        }
    }
}
