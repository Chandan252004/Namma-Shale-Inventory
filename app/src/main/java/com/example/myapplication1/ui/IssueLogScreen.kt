package com.example.myapplication1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication1.data.Issue
import com.example.myapplication1.ui.theme.HeaderBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IssueLogScreen(viewModel: InventoryViewModel) {
    val issues by viewModel.allIssues.collectAsState()
    var showAddIssueDialog by remember { mutableStateOf(false) }

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
                        Text(text = "Issue log", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(text = "${issues.size} events", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddIssueDialog = true }, 
                containerColor = HeaderBlue, 
                contentColor = Color.White, 
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Issue")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F3F4)),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 100.dp
            )
        ) {
            items(issues) { issue ->
                IssueItem(issue)
            }
        }
    }

    if (showAddIssueDialog) {
        AddIssueDialog(
            onDismiss = { showAddIssueDialog = false },
            onAdd = { assetName, category, type, desc, severity ->
                viewModel.addIssue(
                    Issue(
                        assetId = 0, 
                        assetName = assetName, 
                        type = type, 
                        category = category, 
                        description = desc, 
                        severity = severity,
                        date = System.currentTimeMillis()
                    )
                )
                showAddIssueDialog = false
            }
        )
    }
}

@Composable
fun AddIssueDialog(onDismiss: () -> Unit, onAdd: (String, String, String, String, String) -> Unit) {
    var assetName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Lab equipment") }
    var type by remember { mutableStateOf("Damaged") }
    var desc by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf("High") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log New Issue", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = assetName, onValueChange = { assetName = it }, label = { Text("Asset Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                
                Text("Category:", fontWeight = FontWeight.Bold)
                val categories = listOf("Lab equipment", "Sports", "ICT", "Furniture")
                categories.forEach { cat ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = category == cat, onClick = { category = cat })
                        Text(cat)
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                Text("Issue Type:", fontWeight = FontWeight.Bold)
                val types = listOf("Lost", "Damaged", "Misplaced")
                types.forEach { t ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = type == t, onClick = { type = t })
                        Text(t)
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = { if (assetName.isNotBlank()) onAdd(assetName, category, type, desc, severity) }, 
                colors = ButtonDefaults.buttonColors(containerColor = HeaderBlue)
            ) { Text("Log Issue") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) }
        }
    )
}

@Composable
fun IssueItem(issue: Issue) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateString = dateFormat.format(Date(issue.date))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = issue.assetName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                Text(text = dateString, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusBadge(status = issue.type)
                CategoryBadge(category = issue.category)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "\"${issue.description}\"",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
