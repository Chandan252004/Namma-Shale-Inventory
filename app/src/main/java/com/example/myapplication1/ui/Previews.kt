package com.example.myapplication1.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication1.ui.theme.MyApplication1Theme

@Preview(showBackground = true)
@Composable
fun StatusBadgePreview() {
    MyApplication1Theme {
        StatusBadge(status = "Working")
    }
}

@Preview(showBackground = true)
@Composable
fun StatCardPreview() {
    MyApplication1Theme {
        StatCard(label = "Total assets", count = 25, subtext = "Registered items")
    }
}
