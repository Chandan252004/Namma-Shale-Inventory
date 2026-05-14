package com.example.myapplication1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication1.data.Asset
import com.example.myapplication1.data.Issue
import com.example.myapplication1.ui.theme.HeaderBlue

@Composable
fun DashboardScreen(viewModel: InventoryViewModel) {
    val total by viewModel.totalCount.collectAsState()
    val working by viewModel.workingCount.collectAsState()
    val attention by viewModel.attentionCount.collectAsState()
    val broken by viewModel.brokenCount.collectAsState()
    val issues by viewModel.allIssues.collectAsState()
    var showQuickAdd by remember { mutableStateOf(false) }

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
                    Text(text = "Namma-Shaale", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Good morning, Teacher", 
                        color = Color.White, 
                        style = MaterialTheme.typography.headlineMedium, 
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Govt. Primary School, Bengaluru", 
                        color = Color.White.copy(alpha = 0.7f), 
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showQuickAdd = true }, 
                containerColor = HeaderBlue, 
                contentColor = Color.White, 
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F3F4)),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding() + 100.dp // Extra space for FAB
            )
        ) {
            item {
                val workingPercent = if (total > 0) (working * 100 / total) else 0
                val attentionPercent = if (total > 0) (attention * 100 / total) else 0
                val brokenPercent = if (total > 0) (broken * 100 / total) else 0

                Column(modifier = Modifier.padding(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        StatCard("Total assets", total, "Registered items")
                        StatCard("Working", working, "$workingPercent%", percentage = "$workingPercent%")
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        StatCard("Needs attention", attention, "$attentionPercent%", percentage = "$attentionPercent%")
                        StatCard("Broken", broken, "$brokenPercent%", percentage = "$brokenPercent%")
                    }
                }
            }

            item {
                CategoryBreakdownSection()
            }

            item {
                Text(
                    text = "RECENT ACTIVITY",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            }

            if (issues.isEmpty()) {
                item {
                    Text(
                        text = "No recent activity recorded.",
                        modifier = Modifier.padding(20.dp),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(issues.take(15)) { issue ->
                    ActivityItem(issue)
                }
            }
        }
    }

    if (showQuickAdd) {
        AddAssetDialog(
            onDismiss = { showQuickAdd = false },
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
                showQuickAdd = false
            }
        )
    }
}

@Composable
fun CategoryBreakdownSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "CATEGORY BREAKDOWN", style = MaterialTheme.typography.labelLarge, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            CategoryRow("Lab equipment", 18, 0.7f, Color(0xFF1A5298))
            CategoryRow("Sports", 14, 0.5f, Color(0xFF1E8E3E))
            CategoryRow("ICT", 12, 0.4f, Color(0xFF8E24AA))
            CategoryRow("Furniture", 6, 0.2f, Color(0xFFE64A19))
        }
    }
}

@Composable
fun CategoryRow(label: String, count: Int, progress: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(text = count.toString(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            color = color,
            trackColor = color.copy(alpha = 0.1f),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

@Composable
fun ActivityItem(issue: com.example.myapplication1.data.Issue) {
    val dateFormat = java.text.SimpleDateFormat("dd MMM, hh:mm a", java.util.Locale.getDefault())
    val dateString = dateFormat.format(java.util.Date(issue.date))

    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        when(issue.type) {
                            "Broken", "Damaged", "Lost" -> Color.Red
                            "Needs attention", "Attention", "Misplaced" -> Color.Yellow
                            "Working", "Added" -> Color.Green
                            else -> Color.Gray
                        }, 
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${issue.assetName} — ${issue.type}", 
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = issue.description, 
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Text(
                    text = dateString, 
                    style = MaterialTheme.typography.labelSmall, 
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
