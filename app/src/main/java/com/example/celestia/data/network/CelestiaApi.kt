package com.example.celestia.data.network

import com.example.celestia.data.model.KpReading
import retrofit2.http.GET

interface CelestiaApi {
    @GET("planetary_k_index_1m.json")
    suspend fun getKpIndex(): List<KpReading>
}
