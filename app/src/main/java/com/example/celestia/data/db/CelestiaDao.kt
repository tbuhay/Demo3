package com.example.celestia.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.celestia.data.model.IssReading
import com.example.celestia.data.model.KpReading
import kotlinx.coroutines.flow.Flow

@Dao interface CelestiaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(readings: List<KpReading>)

    @Query("SELECT * FROM kp_readings ORDER BY timestamp DESC")
    fun getAll(): Flow<List<KpReading>>

    @Query("DELETE FROM kp_readings")
    suspend fun clear()

    @Query("SELECT * FROM iss_reading LIMIT 1")
    fun getIssReading(): Flow<IssReading?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIssReading(reading: IssReading)
}
