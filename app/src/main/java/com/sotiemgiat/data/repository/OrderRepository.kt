package com.sotiemgiat.data.repository

import androidx.room.withTransaction
import com.sotiemgiat.data.local.database.SoGiatDatabase
import com.sotiemgiat.data.local.entity.OrderEntity
import com.sotiemgiat.data.local.entity.OrderItemEntity
import com.sotiemgiat.data.local.entity.PaymentEntity
import com.sotiemgiat.data.local.entity.ServiceEntity
import kotlinx.coroutines.flow.Flow

class OrderRepository(
    private val database: SoGiatDatabase
) {
    fun observeOrders(): Flow<List<OrderEntity>> =
        database.orderDao().observeAll()

    fun observeServices(): Flow<List<ServiceEntity>> =
        database.serviceDao().observeActive()

    suspend fun createOrder(
        order: OrderEntity,
        items: List<OrderItemEntity>,
        payment: PaymentEntity?
    ): Long = database.withTransaction {
        val orderId = database.orderDao().insert(order)
        database.orderDao().insertItems(
            items.map { it.copy(orderId = orderId) }
        )
        payment?.let {
            database.paymentDao().insert(it.copy(orderId = orderId))
        }
        orderId
    }
}
