package com.example.spaceweathermonitor.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.spaceweathermonitor.data.model.KpReading

@Database(entities = [KpReading::class], version = 1, exportSchema = false)
abstract class SpaceWeatherDatabase : RoomDatabase() {

    abstract fun dao(): SpaceWeatherDao

    companion object {
        @Volatile private var INSTANCE: SpaceWeatherDatabase? = null

        fun getInstance(context: Context): SpaceWeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SpaceWeatherDatabase::class.java,
                    "space_weather_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}