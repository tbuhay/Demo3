package com.example.celestia.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val NOAA_BASE_URL = "https://services.swpc.noaa.gov/json/"
    private const val ISS_BASE_URL = "https://api.wheretheiss.at/"

    val noaaApi: NoaaApi by lazy {
        Retrofit.Builder()
            .baseUrl(NOAA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoaaApi::class.java)
    }

    val issApi: IssApi by lazy {
        Retrofit.Builder()
            .baseUrl(ISS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IssApi::class.java)
    }
}