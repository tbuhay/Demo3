package com.example.celestia.data.network

import com.example.celestia.data.model.IssReading
import retrofit2.http.GET

interface IssApi {
    // Public endpoint from WhereTheISS.at
    @GET("v1/satellites/25544")
    suspend fun getIssPosition(): IssReading
}