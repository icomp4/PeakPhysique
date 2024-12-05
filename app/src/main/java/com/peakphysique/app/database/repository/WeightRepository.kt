package com.peakphysique.app.database.repository

import com.peakphysique.app.database.dao.WeightDao
import com.peakphysique.app.model.WeightEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class WeightRepository(private val weightDao: WeightDao) {
    val allWeights: Flow<List<WeightEntity>> = weightDao.getAllWeights()

    suspend fun insertWeight(weight: Float) {
        val weightLog = WeightEntity(
            weight = weight,
            date = LocalDateTime.now()
        )
        weightDao.insertWeight(weightLog)
    }

    suspend fun getLatestWeight(): WeightEntity? {
        return weightDao.getLatestWeight()
    }

    fun getWeightsInRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WeightEntity>> {
        return weightDao.getWeightsInRange(startDate, endDate)
    }
}