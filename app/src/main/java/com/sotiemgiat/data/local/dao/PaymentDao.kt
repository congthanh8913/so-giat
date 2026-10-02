package com.sotiemgiat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.sotiemgiat.data.local.entity.PaymentEntity

@Dao
interface PaymentDao {
    @Insert
    suspend fun insert(payment: PaymentEntity): Long
}
