package com.example.myapplication1.data

import kotlinx.coroutines.flow.Flow

class InventoryRepository(private val inventoryDao: InventoryDao) {
    val allAssets: Flow<List<Asset>> = inventoryDao.getAllAssets()
    val allIssues: Flow<List<Issue>> = inventoryDao.getAllIssues()
    val allHealthChecks: Flow<List<HealthCheck>> = inventoryDao.getAllHealthChecks()
    
    val totalAssetsCount: Flow<Int> = inventoryDao.getAssetCount()
    val workingAssetsCount: Flow<Int> = inventoryDao.getWorkingCount()
    val needsAttentionAssetsCount: Flow<Int> = inventoryDao.getNeedsAttentionCount()
    val brokenAssetsCount: Flow<Int> = inventoryDao.getBrokenCount()

    suspend fun insertAsset(asset: Asset) = inventoryDao.insertAsset(asset)
    suspend fun updateAsset(asset: Asset) = inventoryDao.updateAsset(asset)
    suspend fun deleteAsset(asset: Asset) = inventoryDao.deleteAsset(asset)
    suspend fun getAssetById(id: Int) = inventoryDao.getAssetById(id)

    suspend fun insertIssue(issue: Issue) = inventoryDao.insertIssue(issue)
    suspend fun insertHealthCheck(healthCheck: HealthCheck) = inventoryDao.insertHealthCheck(healthCheck)

    suspend fun getUserByUsername(username: String) = inventoryDao.getUserByUsername(username)
    suspend fun insertUser(user: User) = inventoryDao.insertUser(user)
    suspend fun getUserCount() = inventoryDao.getUserCount()
}
