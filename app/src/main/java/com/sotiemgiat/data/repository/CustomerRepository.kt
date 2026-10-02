package com.sotiemgiat.data.repository

import com.sotiemgiat.data.local.dao.CustomerDao
import com.sotiemgiat.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

class CustomerRepository(
    private val dao: CustomerDao
) {
    fun observeCustomers(): Flow<List<CustomerEntity>> = dao.observeAll()

    suspend fun addCustomer(name: String, phone: String): Long =
        dao.insert(CustomerEntity(name = name, phone = phone))
}
