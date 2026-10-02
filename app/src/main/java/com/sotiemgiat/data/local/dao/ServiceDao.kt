package com.sotiemgiat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sotiemgiat.data.local.entity.ServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services WHERE isActive = 1 ORDER BY name COLLATE NOCASE")
    fun observeActive(): Flow<List<ServiceEntity>>

    @Insert
    suspend fun insert(service: ServiceEntity): Long

    @Query("SELECT COUNT(*) FROM services")
    suspend fun count(): Int
}
