package com.sotiemgiat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val customerId: Long,
    val receivedAt: Long,
    val dueAt: Long?,
    val completedAt: Long?,
    val deliveredAt: Long?,
    val status: String = "RECEIVED",
    val subtotal: Long = 0,
    val discount: Long = 0,
    val total: Long = 0,
    val paidAmount: Long = 0,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
