package com.example.celestia.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "iss_reading")
data class IssReading(
    @PrimaryKey val id: Int = 1, // always 1 → one row that gets replaced each refresh
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val velocity: Double,
    val timestamp: String
)