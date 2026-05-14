package com.example.myapplication1.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val serialNumber: String,
    val category: String, // Lab, Sports, ICT, Furniture
    val status: String, // Working, Needs attention, Broken
    val lastChecked: Long = System.currentTimeMillis(),
    val description: String? = null,
    val photoUri: String? = null
)

@Entity(tableName = "issues")
data class Issue(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val assetId: Int = 0,
    val assetName: String,
    val type: String, // Lost, Damaged, Misplaced, Added
    val category: String,
    val description: String,
    val date: Long = System.currentTimeMillis(),
    val severity: String // High, Medium, Low
)

@Entity(tableName = "health_checks")
data class HealthCheck(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val itemsChecked: Int,
    val summary: String
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String // In a real app, this should be hashed
)
