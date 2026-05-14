package com.example.myapplication1.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Query("SELECT * FROM assets ORDER BY id DESC")
    fun getAllAssets(): Flow<List<Asset>>

    @Query("SELECT * FROM assets WHERE id = :id")
    suspend fun getAssetById(id: Int): Asset?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: Asset): Long

    @Update
    suspend fun updateAsset(asset: Asset): Int

    @Delete
    suspend fun deleteAsset(asset: Asset): Int

    @Query("SELECT * FROM issues ORDER BY date DESC")
    fun getAllIssues(): Flow<List<Issue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIssue(issue: Issue): Long

    @Query("SELECT * FROM health_checks ORDER BY date DESC")
    fun getAllHealthChecks(): Flow<List<HealthCheck>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthCheck(healthCheck: HealthCheck): Long

    @Query("SELECT COUNT(*) FROM assets")
    fun getAssetCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM assets WHERE status = 'Working'")
    fun getWorkingCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM assets WHERE status = 'Needs attention'")
    fun getNeedsAttentionCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM assets WHERE status = 'Broken'")
    fun getBrokenCount(): Flow<Int>

    // User table operations
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}
