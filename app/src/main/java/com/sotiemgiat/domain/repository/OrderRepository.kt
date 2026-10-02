package com.sotiemgiat.domain.repository

import com.sotiemgiat.data.local.entity.OrderEntity
import com.sotiemgiat.data.local.entity.OrderItemEntity
import com.sotiemgiat.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun observeOrders(): Flow<List<OrderEntity>>
    suspend fun createOrder(order: OrderEntity, items: List<OrderItemEntity>, payment: PaymentEntity?): Long
}
