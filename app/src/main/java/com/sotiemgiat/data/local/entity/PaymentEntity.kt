package com.sotiemgiat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val amount: Long,
    val method: String = "CASH",
    val paidAt: Long = System.currentTimeMillis(),
    val note: String = ""
)
