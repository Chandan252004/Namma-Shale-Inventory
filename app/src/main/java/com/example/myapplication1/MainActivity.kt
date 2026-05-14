package com.example.myapplication1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication1.data.InventoryDatabase
import com.example.myapplication1.data.InventoryRepository
import com.example.myapplication1.data.User
import com.example.myapplication1.ui.InventoryViewModel
import com.example.myapplication1.ui.InventoryViewModelFactory
import com.example.myapplication1.ui.MainScreen
import com.example.myapplication1.ui.theme.MyApplication1Theme
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = InventoryDatabase.getDatabase(this)
        val repository = InventoryRepository(database.inventoryDao())
        val factory = InventoryViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            MyApplication1Theme {
                val viewModel: InventoryViewModel = viewModel(factory = factory)

                // Add sample data if empty
                LaunchedEffect(Unit) {
                    // Check for default users
                    if (repository.getUserCount() == 0) {
                        repository.insertUser(User(username = "admin", password = "1234"))
                        repository.insertUser(User(username = "Chandan", password = "1234"))
                    }

                    val assets = repository.allAssets.first()
                    if (assets.isEmpty()) {
                        repository.insertAsset(com.example.myapplication1.data.Asset(name = "Microscope #1", serialNumber = "LAB-2022-001", category = "Lab equipment", status = "Working"))
                        repository.insertAsset(com.example.myapplication1.data.Asset(name = "Football", serialNumber = "SPT-2022-041", category = "Sports", status = "Broken"))
                        repository.insertAsset(com.example.myapplication1.data.Asset(name = "Tablet #4", serialNumber = "ICT-2023-004", category = "ICT", status = "Working"))
                        repository.insertAsset(com.example.myapplication1.data.Asset(name = "Test tube set", serialNumber = "LAB-2022-018", category = "Lab equipment", status = "Needs attention"))
                        repository.insertAsset(com.example.myapplication1.data.Asset(name = "Badminton set", serialNumber = "SPT-2022-012", category = "Sports", status = "Working"))
                        repository.insertAsset(com.example.myapplication1.data.Asset(name = "Projector", serialNumber = "ICT-2021-003", category = "ICT", status = "Needs attention"))

                        repository.insertIssue(com.example.myapplication1.data.Issue(assetId = 2, assetName = "Football", type = "Lost", category = "Sports", description = "Lost during inter-school match at playground. Last seen with Class 7B.", severity = "High"))
                        repository.insertIssue(com.example.myapplication1.data.Issue(assetId = 6, assetName = "Projector", type = "Damaged", category = "ICT", description = "Lamp flickering intermittently. Needs bulb replacement.", severity = "Medium"))
                    }
                }

                MainScreen(viewModel)
            }
        }
    }
}
