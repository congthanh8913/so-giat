package com.sotiemgiat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sotiemgiat.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<CustomerEntity>>

    @Insert
    suspend fun insert(customer: CustomerEntity): Long

    @Query("SELECT * FROM customers WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): CustomerEntity?

    @Query("SELECT * FROM customers WHERE phone = :phone LIMIT 1")
    suspend fun getByPhone(phone: String): CustomerEntity?
}
