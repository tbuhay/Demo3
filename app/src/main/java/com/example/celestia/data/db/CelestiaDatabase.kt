package com.example.celestia.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.celestia.data.model.KpReading

@Database(entities = [KpReading::class], version = 1, exportSchema = false)
abstract class CelestiaDatabase : RoomDatabase() {

    abstract fun dao(): CelestiaDao

    companion object {
        @Volatile private var INSTANCE: CelestiaDatabase? = null

        fun getInstance(context: Context): CelestiaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CelestiaDatabase::class.java,
                    "celestia_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}