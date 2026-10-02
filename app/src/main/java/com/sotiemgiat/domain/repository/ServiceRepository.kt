package com.sotiemgiat.domain.repository

import com.sotiemgiat.data.local.entity.ServiceEntity
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    fun observeActiveServices(): Flow<List<ServiceEntity>>
    suspend fun count(): Int
    suspend fun addService(service: ServiceEntity): Long
}
