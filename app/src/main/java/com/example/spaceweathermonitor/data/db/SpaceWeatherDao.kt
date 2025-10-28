package com.example.spaceweathermonitor.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spaceweathermonitor.data.model.KpReading
import kotlinx.coroutines.flow.Flow

@Dao interface SpaceWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(readings: List<KpReading>)

    @Query("SELECT * FROM kp_readings ORDER BY timestamp DESC")
    fun getAll(): Flow<List<KpReading>>

    @Query("DELETE FROM kp_readings")
    suspend fun clear()
}
