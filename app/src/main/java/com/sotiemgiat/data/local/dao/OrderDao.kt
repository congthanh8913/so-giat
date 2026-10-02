package com.sotiemgiat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sotiemgiat.data.local.entity.OrderEntity
import com.sotiemgiat.data.local.entity.OrderItemEntity
import com.sotiemgiat.data.local.model.OrderListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: OrderEntity): Long

    @Insert
    suspend fun insertItems(items: List<OrderItemEntity>)

    @Query("SELECT * FROM orders ORDER BY receivedAt DESC")
    fun observeAll(): Flow<List<OrderEntity>>

    @Query("""
        SELECT
            o.id AS id,
            o.orderNumber AS orderNumber,
            c.name AS customerName,
            c.phone AS customerPhone,
            o.receivedAt AS receivedAt,
            o.dueAt AS dueAt,
            o.status AS status,
            o.total AS total,
            o.paidAmount AS paidAmount
        FROM orders o
        INNER JOIN customers c ON c.id = o.customerId
        ORDER BY o.receivedAt DESC
    """)
    fun observeOrderList(): Flow<List<OrderListItem>>
}
