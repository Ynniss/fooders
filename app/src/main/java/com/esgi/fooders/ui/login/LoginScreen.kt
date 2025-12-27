package com.esgi.fooders.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.fooders.R
import com.esgi.fooders.ui.components.FoodersCard
import com.esgi.fooders.ui.components.FoodersFullWidthButton
import com.esgi.fooders.ui.components.FoodersPasswordField
import com.esgi.fooders.ui.components.FoodersTextField
import com.esgi.fooders.ui.theme.FoodersTheme
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val loginState by viewModel.loginEvent.observeAsState(LoginViewModel.LoginEvent.Empty)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginViewModel.LoginEvent.Success -> {
                isLoading = false
                onLoginSuccess()
            }
            is LoginViewModel.LoginEvent.Failure -> {
                isLoading = false
                Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
            }
            is LoginViewModel.LoginEvent.Loading -> {
                isLoading = true
            }
            is LoginViewModel.LoginEvent.Empty -> {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Surface(
                modifier = Modifier.size(120.dp),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_round),
                    contentDescription = "Fooders Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Login Card
            FoodersCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FoodersTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Nom d'utilisateur",
                        leadingIcon = Icons.Default.Person,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FoodersPasswordField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Mot de passe",
                        leadingIcon = Icons.Default.Lock,
                        imeAction = ImeAction.Done,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    FoodersFullWidthButton(
                        text = "Connexion",
                        onClick = {
                            if (username.isNotBlank() && password.isNotBlank()) {
                                FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                                    viewModel.login(username, password, token)
                                }.addOnFailureListener {
                                    viewModel.login(username, password, "")
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Veuillez remplir tous les champs",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        isLoading = isLoading,
                        enabled = username.isNotBlank() && password.isNotBlank()
                    )
                }
            }
        }
    }
}
