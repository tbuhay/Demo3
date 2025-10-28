package com.example.spaceweathermonitor.data.repository

import android.util.Log
import com.example.spaceweathermonitor.data.db.SpaceWeatherDao
import com.example.spaceweathermonitor.data.model.KpReading
import com.example.spaceweathermonitor.data.network.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class SpaceWeatherRepository(private val dao: SpaceWeatherDao) {

    val readings: Flow<List<KpReading>> = dao.getAll()

    suspend fun refreshData() {
        try {
            val newData = RetrofitInstance.api.getKpIndex()
            dao.insertAll(newData)
            Log.d("SpaceWeatherRepo", "Inserted ${newData.size} readings (full data set)")
        } catch (e: Exception) {
            Log.e("SpaceWeatherRepo", "Error fetching data", e)
        }
    }
}