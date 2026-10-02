package com.sotiemgiat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val serviceId: Long?,
    val serviceNameSnapshot: String,
    val quantity: Double,
    val unitPrice: Long,
    val total: Long,
    val note: String = ""
)
