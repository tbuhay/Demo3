package com.example.spaceweathermonitor.data.network

import com.example.spaceweathermonitor.data.model.KpReading
import retrofit2.http.GET

interface SpaceWeatherApi {
    @GET("planetary_k_index_1m.json")
    suspend fun getKpIndex(): List<KpReading>
}