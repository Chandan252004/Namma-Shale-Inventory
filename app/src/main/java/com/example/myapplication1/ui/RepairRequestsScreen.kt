package com.example.myapplication1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication1.data.Asset
import com.example.myapplication1.ui.theme.HeaderBlue
import kotlinx.coroutines.launch

@Composable
fun RepairRequestsScreen(viewModel: InventoryViewModel) {
    val assets by viewModel.allAssets.collectAsState()
    val repairItems = assets.filter { it.status == "Broken" || it.status == "Needs attention" }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderBlue)
                    .padding(16.dp)
                    .padding(top = 32.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Repair requests", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Surface(color = Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(16.dp)) {
                        Text(
                            text = "${repairItems.size} items",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        bottomBar = {
            Button(
                onClick = { 
                    scope.launch {
                        snackbarHostState.showSnackbar("Generating PDF report for SDMC...")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HeaderBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Export PDF & share with SDMC", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF1F3F4))
        ) {
            item {
                RepairSummaryCard(repairItems.size)
            }

            val critical = repairItems.filter { it.status == "Broken" }
            val moderate = repairItems.filter { it.status == "Needs attention" }

            if (critical.isNotEmpty()) {
                item { SectionHeader("CRITICAL — BROKEN") }
                items(critical) { item -> RepairItem(item) }
            }

            if (moderate.isNotEmpty()) {
                item { SectionHeader("MODERATE — NEEDS ATTENTION") }
                items(moderate) { item -> RepairItem(item) }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun RepairSummaryCard(count: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = HeaderBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = count.toString(), color = Color.White, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold)
                Text(text = "Items pending SDMC attention", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "5 critical", color = Color.White, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(text = "7 moderate", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 12.dp),
        style = MaterialTheme.typography.labelLarge,
        color = Color.DarkGray,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun RepairItem(asset: Asset) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(if (asset.status == "Broken") Color(0xFFFCE8E6) else Color(0xFFFEF7E0), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                val emoji = when (asset.category) {
                    "Lab equipment" -> "🔬"
                    "Sports" -> "⚽"
                    "ICT" -> "💻"
                    else -> "🪑"
                }
                Text(text = emoji, style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = asset.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "S/N: ${asset.serialNumber}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Issue logged • 12 Jan 2025", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            }
            StatusBadge(status = if (asset.status == "Broken") "High" else "Medium")
        }
    }
}
