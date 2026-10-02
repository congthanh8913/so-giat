package com.sotiemgiat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.sotiemgiat.data.local.entity.OrderEntity
import com.sotiemgiat.data.local.entity.OrderItemEntity

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: OrderEntity): Long

    @Insert
    suspend fun insertItems(items: List<OrderItemEntity>)

    @androidx.room.Query("SELECT * FROM orders ORDER BY receivedAt DESC")
    fun observeAll(): kotlinx.coroutines.flow.Flow<List<OrderEntity>>
}
