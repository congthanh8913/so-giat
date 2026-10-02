package com.sotiemgiat.data.repository

import androidx.room.withTransaction
import com.sotiemgiat.data.local.database.SoGiatDatabase
import com.sotiemgiat.data.local.entity.OrderEntity
import com.sotiemgiat.data.local.entity.OrderItemEntity
import com.sotiemgiat.data.local.entity.PaymentEntity
import com.sotiemgiat.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class OrderRepositoryImpl(private val database: SoGiatDatabase) : OrderRepository {
    override fun observeOrders(): Flow<List<OrderEntity>> = database.orderDao().observeAll()

    override suspend fun createOrder(
        order: OrderEntity,
        items: List<OrderItemEntity>,
        payment: PaymentEntity?
    ): Long = database.withTransaction {
        val orderId = database.orderDao().insert(order)
        database.orderDao().insertItems(items.map { it.copy(orderId = orderId) })
        payment?.let { database.paymentDao().insert(it.copy(orderId = orderId)) }
        orderId
    }
}
