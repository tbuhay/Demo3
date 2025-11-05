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

    val readings = repo.readings.asLiveData()

    private val _lastUpdated = MutableLiveData<String>()
    val lastUpdated: LiveData<String> = _lastUpdated


    fun refresh() {
        viewModelScope.launch {
            repo.refreshData()

            val formattedTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val now = java.time.ZonedDateTime.now()
                val formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm 'UTC'")
                now.format(formatter)
            } else {
                val sdf = SimpleDateFormat("HH:mm 'UTC'", Locale.US)
                sdf.format(Date())
            }

            _lastUpdated.postValue(formattedTime)
        }
    }

}