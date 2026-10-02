package com.sotiemgiat.data.repository

import com.sotiemgiat.data.local.dao.ServiceDao
import com.sotiemgiat.data.local.entity.ServiceEntity
import com.sotiemgiat.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow

class ServiceRepositoryImpl(private val dao: ServiceDao) : ServiceRepository {
    override fun observeActiveServices(): Flow<List<ServiceEntity>> = dao.observeActive()
    override suspend fun count(): Int = dao.count()
    override suspend fun addService(service: ServiceEntity): Long = dao.insert(service)
}
