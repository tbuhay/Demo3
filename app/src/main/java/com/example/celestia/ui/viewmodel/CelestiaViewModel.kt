package com.example.celestia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.celestia.data.db.CelestiaDatabase
import com.example.celestia.data.repository.CelestiaRepository
import kotlinx.coroutines.launch
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

class CelestiaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = CelestiaDatabase.getInstance(application).dao()
    private val repo = CelestiaRepository(dao)
    private val prefs = application.getSharedPreferences("celestia_prefs", 0)

    val readings = repo.readings.asLiveData()

    private val _lastUpdated = MutableLiveData<String>()
    val lastUpdated: LiveData<String> = _lastUpdated

    init {
        // Load persisted time from SharedPreferences
        _lastUpdated.value = prefs.getString("last_updated", "Never")
    }

    /** Refresh NOAA data and persist timestamp */
    fun refresh() {
        viewModelScope.launch {
            repo.refreshData()

            val formattedTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val now = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
                val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM d, HH:mm 'UTC'")
                now.format(formatter)
            } else {
                val sdf = SimpleDateFormat("MMM d, HH:mm 'UTC'", Locale.US)
                sdf.format(Date())
            }

            prefs.edit().putString("last_updated", formattedTime).apply()
            _lastUpdated.postValue(formattedTime)
        }
    }

    /** Format NOAA time_tag values like 2025-11-05T18:00:00Z -> Nov 5, 18:00 UTC */
    fun formatKpTimestamp(raw: String?): String {
        if (raw.isNullOrBlank()) return "-"
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val date = parser.parse(raw)
            val formatter = SimpleDateFormat("MMM d, HH:mm 'UTC'", Locale.US)
            formatter.format(date ?: Date())
        } catch (e: Exception) {
            raw
        }
    }
}
