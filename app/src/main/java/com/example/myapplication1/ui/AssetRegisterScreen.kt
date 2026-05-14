package com.example.myapplication1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication1.data.Asset
import com.example.myapplication1.data.Issue
import com.example.myapplication1.ui.theme.HeaderBlue

@Composable
fun AssetRegisterScreen(viewModel: InventoryViewModel) {
    val assets by viewModel.allAssets.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredAssets = assets.filter { asset ->
        (selectedCategory == "All" || asset.category.contains(selectedCategory, ignoreCase = true)) &&
        (searchQuery.isEmpty() || asset.name.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            Surface(
                color = HeaderBlue,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(top = 32.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Asset register", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(text = "${filteredAssets.size} items", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search assets...", color = Color.White.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                            focusedContainerColor = Color.White.copy(alpha = 0.15f),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = HeaderBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Asset")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F3F4))
        ) {
            // Apply padding manually since LazyVerticalGrid might need custom padding
            Spacer(modifier = Modifier.height(padding.calculateTopPadding()))
            
            FilterChips(selectedCategory) { selectedCategory = it }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(
                    bottom = padding.calculateBottomPadding() + 100.dp
                )
            ) {
                items(filteredAssets) { asset ->
                    AssetCard(asset)
                }
            }
        }
    }

    if (showAddDialog) {
        AddAssetDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, sn, cat, cond ->
                val newAsset = Asset(name = name, serialNumber = sn, category = cat, status = cond)
                viewModel.addAsset(newAsset)
                
                viewModel.addIssue(
                    Issue(
                        assetId = 0,
                        assetName = name,
                        type = if (cond == "Working") "Added" else cond,
                        category = cat,
                        description = "New asset $name added to $cat register",
                        severity = if (cond == "Broken") "High" else "Medium",
                        date = System.currentTimeMillis()
                    )
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddAssetDialog(onDismiss: () -> Unit, onAdd: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var sn by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Lab equipment") }
    var condition by remember { mutableStateOf("Working") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Asset", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Item Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = sn, onValueChange = { sn = it }, label = { Text("Serial Number") }, modifier = Modifier.fillMaxWidth())
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Category:", fontWeight = FontWeight.Bold)
                val categories = listOf("Lab equipment", "Sports", "ICT", "Furniture")
                categories.forEach { cat ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        RadioButton(selected = category == cat, onClick = { category = cat })
                        Text(cat)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Condition:", fontWeight = FontWeight.Bold)
                val conditions = listOf("Working", "Needs attention", "Broken")
                conditions.forEach { cond ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        RadioButton(selected = condition == cond, onClick = { condition = cond })
                        Text(cond)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onAdd(name, sn, category, condition) },
                colors = ButtonDefaults.buttonColors(containerColor = HeaderBlue)
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) }
        }
    )
}

@Composable
fun FilterChips(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val categories = listOf("All", "Lab", "Sports", "ICT", "Furniture")
        categories.forEach { category ->
            val isSelected = selectedCategory == category
            Surface(
                modifier = Modifier.height(36.dp),
                onClick = { onCategorySelected(category) },
                color = if (isSelected) HeaderBlue else Color.White,
                shape = RoundedCornerShape(18.dp),
                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
            ) {
                Box(modifier = Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else Color.Gray,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AssetCard(asset: Asset) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                val emoji = when {
                    asset.category.contains("Lab", true) -> "🔬"
                    asset.category.contains("Sports", true) -> "⚽"
                    asset.category.contains("ICT", true) -> "💻"
                    else -> "🪑"
                }
                Text(text = emoji, style = TextStyle(fontSize = 40.sp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = asset.name, 
                style = MaterialTheme.typography.titleMedium, 
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                color = Color.Black
            )
            Text(text = asset.category, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            StatusBadge(status = asset.status)
        }
    }
}
