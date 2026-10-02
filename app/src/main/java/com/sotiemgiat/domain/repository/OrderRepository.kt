package com.sotiemgiat.domain.repository

import com.sotiemgiat.data.local.entity.OrderEntity
import com.sotiemgiat.data.local.entity.OrderItemEntity
import com.sotiemgiat.data.local.entity.PaymentEntity
import com.sotiemgiat.data.local.model.OrderListItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun observeOrders(): Flow<List<OrderEntity>>
    fun observeOrderList(): Flow<List<OrderListItem>>
    suspend fun createOrder(order: OrderEntity, items: List<OrderItemEntity>, payment: PaymentEntity?): Long
}
