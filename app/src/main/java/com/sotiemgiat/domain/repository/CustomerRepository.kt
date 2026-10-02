package com.sotiemgiat.domain.repository

import com.sotiemgiat.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun observeCustomers(): Flow<List<CustomerEntity>>
    suspend fun findByPhone(phone: String): CustomerEntity?
    suspend fun addCustomer(name: String, phone: String): Long
}
