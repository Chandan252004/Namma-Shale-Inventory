package com.example.myapplication1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication1.ui.theme.*

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        "Working" -> StatusGreenBg to StatusGreenText
        "Needs attention", "Attention", "Moderate" -> StatusYellowBg to StatusYellowText
        "Broken", "High", "Damaged", "Lost" -> StatusRedBg to StatusRedText
        else -> StatusBlueBg to StatusBlueText
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = status,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CategoryBadge(category: String) {
    Surface(
        color = StatusBlueBg,
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = category,
            color = StatusBlueText,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StatCard(label: String, count: Int, subtext: String, percentage: String? = null) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .padding(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count.toString(), 
                style = MaterialTheme.typography.headlineLarge, 
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(text = subtext, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            
            if (percentage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                StatusBadge(status = percentage)
            }
        }
    }
}
