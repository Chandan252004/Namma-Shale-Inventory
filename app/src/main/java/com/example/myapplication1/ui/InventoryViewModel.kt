package com.example.myapplication1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication1.data.Asset
import com.example.myapplication1.data.HealthCheck
import com.example.myapplication1.data.InventoryRepository
import com.example.myapplication1.data.Issue
import com.example.myapplication1.data.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: InventoryRepository) : ViewModel() {

    val allAssets: StateFlow<List<Asset>> = repository.allAssets.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allIssues: StateFlow<List<Issue>> = repository.allIssues.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalCount: StateFlow<Int> = repository.totalAssetsCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val workingCount: StateFlow<Int> = repository.workingAssetsCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val attentionCount: StateFlow<Int> = repository.needsAttentionAssetsCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val brokenCount: StateFlow<Int> = repository.brokenAssetsCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun addAsset(asset: Asset) {
        viewModelScope.launch {
            repository.insertAsset(asset)
        }
    }

    fun updateAsset(asset: Asset) {
        viewModelScope.launch {
            repository.updateAsset(asset)
        }
    }

    fun addIssue(issue: Issue) {
        viewModelScope.launch {
            repository.insertIssue(issue)
        }
    }

    fun addHealthCheck(check: HealthCheck) {
        viewModelScope.launch {
            repository.insertHealthCheck(check)
        }
    }

    suspend fun authenticateUser(username: String, password: String): Boolean {
        val user = repository.getUserByUsername(username)
        return user != null && user.password == password
    }

    fun registerUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }
}
