package com.sotiemgiat.data.repository

import com.sotiemgiat.data.local.dao.CustomerDao
import com.sotiemgiat.data.local.entity.CustomerEntity
import com.sotiemgiat.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow

class CustomerRepositoryImpl(private val dao: CustomerDao) : CustomerRepository {
    override fun observeCustomers(): Flow<List<CustomerEntity>> = dao.observeAll()
    override suspend fun findByPhone(phone: String): CustomerEntity? = dao.getByPhone(phone)
    override suspend fun addCustomer(name: String, phone: String): Long =
        dao.insert(CustomerEntity(name = name, phone = phone))
}
