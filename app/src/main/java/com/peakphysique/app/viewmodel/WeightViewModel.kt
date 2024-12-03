package com.peakphysique.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peakphysique.app.database.AppDatabase
import com.peakphysique.app.database.repository.WeightRepository
import com.peakphysique.app.model.WeightEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeightViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WeightRepository
    val allWeights: Flow<List<WeightEntity>>

    private val _latestWeight = MutableStateFlow<Float?>(null)
    val latestWeight: StateFlow<Float?> = _latestWeight.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = WeightRepository(database.weightDao())
        allWeights = repository.allWeights

        viewModelScope.launch {
            repository.getLatestWeight()?.let {
                _latestWeight.value = it.weight
            }
        }
    }

    fun logWeight(weight: Float) {
        viewModelScope.launch {
            repository.insertWeight(weight)
        }
    }
}