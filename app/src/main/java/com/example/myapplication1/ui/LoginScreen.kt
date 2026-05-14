package com.example.myapplication1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication1.data.User
import com.example.myapplication1.ui.theme.HeaderBlue
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: InventoryViewModel, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var showRegisterDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HeaderBlue),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp)
                .verticalScroll(scrollState),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏫",
                    fontSize = 64.sp
                )
                Text(
                    text = "Namma-Shaale",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = HeaderBlue
                )
                Text(
                    text = "Inventory Management",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                if (error.isNotEmpty()) {
                    Text(text = error, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        scope.launch {
                            val isValid = viewModel.authenticateUser(username, password)
                            if (isValid) {
                                onLoginSuccess()
                            } else {
                                error = "Invalid credentials. Please try again."
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HeaderBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("LOGIN", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("New user?", color = Color.Gray)
                    TextButton(onClick = { showRegisterDialog = true }) {
                        Text("Register", color = HeaderBlue, fontWeight = FontWeight.Bold)
                    }
                }
                
                TextButton(onClick = { }) {
                    Text("Forgot Password?", color = Color.Gray)
                }
            }
        }
    }

    if (showRegisterDialog) {
        RegisterDialog(
            onDismiss = { showRegisterDialog = false },
            onRegister = { newUsername, newPassword ->
                viewModel.registerUser(User(username = newUsername, password = newPassword))
                // Log the registration event in the activity feed
                viewModel.addIssue(
                    com.example.myapplication1.data.Issue(
                        assetName = "System",
                        type = "Added",
                        category = "User",
                        description = "New user registered: $newUsername",
                        severity = "Low"
                    )
                )
                showRegisterDialog = false
            }
        )
    }
}

@Composable
fun RegisterDialog(onDismiss: () -> Unit, onRegister: (String, String) -> Unit) {
    var regUsername by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Register New User", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = regUsername,
                    onValueChange = { regUsername = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = regPassword,
                    onValueChange = { regPassword = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (regUsername.isNotBlank() && regPassword.isNotBlank()) onRegister(regUsername, regPassword) },
                colors = ButtonDefaults.buttonColors(containerColor = HeaderBlue)
            ) { Text("Register") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
