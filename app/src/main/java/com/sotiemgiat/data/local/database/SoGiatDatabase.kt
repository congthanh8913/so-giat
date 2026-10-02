package com.sotiemgiat.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sotiemgiat.data.local.dao.CustomerDao
import com.sotiemgiat.data.local.entity.CustomerEntity

@Database(
    entities = [CustomerEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SoGiatDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
}
