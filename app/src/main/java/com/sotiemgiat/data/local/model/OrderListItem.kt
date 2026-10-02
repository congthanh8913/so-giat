package com.sotiemgiat.data.local.model

data class OrderListItem(
    val id: Long,
    val orderNumber: String,
    val customerName: String,
    val customerPhone: String,
    val receivedAt: Long,
    val dueAt: Long?,
    val status: String,
    val total: Long,
    val paidAmount: Long
)
