package com.example.celestia.data.repository

import android.util.Log
import com.example.celestia.data.db.CelestiaDao
import com.example.celestia.data.model.KpReading
import com.example.celestia.data.network.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class CelestiaRepository(private val dao: CelestiaDao) {

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