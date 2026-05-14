package com.example.myapplication1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication1.data.Asset
import com.example.myapplication1.ui.theme.*

@Composable
fun HealthCheckScreen(viewModel: InventoryViewModel) {
    val assets by viewModel.allAssets.collectAsState()
    val totalCount = assets.size
    val checkedCount = assets.count { it.lastChecked > System.currentTimeMillis() - 3600000 } // Checked in last hour

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
                        Text(text = "Monthly health check", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Surface(color = Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(16.dp)) {
                            Text(
                                text = "$checkedCount / $totalCount",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { if (totalCount > 0) checkedCount.toFloat() / totalCount else 0f },
                        modifier = Modifier.fillMaxWidth().height(10.dp),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tap a status to update. Target: 10 items in 2 min.",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F3F4)),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding() + 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(assets) { asset ->
                HealthCheckItem(
                    asset = asset,
                    onStatusUpdate = { newStatus ->
                        viewModel.updateAsset(asset.copy(status = newStatus, lastChecked = System.currentTimeMillis()))
                        
                        viewModel.addIssue(
                            com.example.myapplication1.data.Issue(
                                assetId = asset.id,
                                assetName = asset.name,
                                type = newStatus,
                                category = asset.category,
                                description = "Status updated to $newStatus during monthly audit",
                                severity = if (newStatus == "Broken") "High" else "Medium",
                                date = System.currentTimeMillis()
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun HealthCheckItem(asset: Asset, onStatusUpdate: (String) -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                val emoji = when {
                    asset.category.contains("Lab", true) -> "🔬"
                    asset.category.contains("Sports", true) -> "⚽"
                    asset.category.contains("ICT", true) -> "💻"
                    else -> "🪑"
                }
                Text(text = emoji, style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = asset.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = asset.category, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusButton(
                    symbol = "✓",
                    color = StatusGreenText,
                    bgColor = StatusGreenBg,
                    isSelected = asset.status == "Working",
                    onClick = { onStatusUpdate("Working") }
                )
                StatusButton(
                    symbol = "!",
                    color = StatusYellowText,
                    bgColor = StatusYellowBg,
                    isSelected = asset.status == "Needs attention",
                    onClick = { onStatusUpdate("Needs attention") }
                )
                StatusButton(
                    symbol = "X",
                    color = StatusRedText,
                    bgColor = StatusRedBg,
                    isSelected = asset.status == "Broken",
                    onClick = { onStatusUpdate("Broken") }
                )
            }
        }
    }
}

@Composable
fun StatusButton(symbol: String, color: Color, bgColor: Color, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) color else bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.size(42.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = symbol,
                color = if (isSelected) Color.White else color,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
