package com.sotiemgiat.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sotiemgiat.data.local.dao.CustomerDao
import com.sotiemgiat.data.local.dao.OrderDao
import com.sotiemgiat.data.local.dao.PaymentDao
import com.sotiemgiat.data.local.dao.ServiceDao
import com.sotiemgiat.data.local.entity.CustomerEntity
import com.sotiemgiat.data.local.entity.OrderEntity
import com.sotiemgiat.data.local.entity.OrderItemEntity
import com.sotiemgiat.data.local.entity.PaymentEntity
import com.sotiemgiat.data.local.entity.ServiceEntity

@Database(
    entities = [
        CustomerEntity::class,
        ServiceEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        PaymentEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SoGiatDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun serviceDao(): ServiceDao
    abstract fun orderDao(): OrderDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS services (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        pricingType TEXT NOT NULL,
                        price INTEGER NOT NULL,
                        unit TEXT NOT NULL,
                        isActive INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS orders (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        orderNumber TEXT NOT NULL,
                        customerId INTEGER NOT NULL,
                        receivedAt INTEGER NOT NULL,
                        dueAt INTEGER,
                        completedAt INTEGER,
                        deliveredAt INTEGER,
                        status TEXT NOT NULL,
                        subtotal INTEGER NOT NULL,
                        discount INTEGER NOT NULL,
                        total INTEGER NOT NULL,
                        paidAmount INTEGER NOT NULL,
                        note TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS order_items (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        orderId INTEGER NOT NULL,
                        serviceId INTEGER,
                        serviceNameSnapshot TEXT NOT NULL,
                        quantity REAL NOT NULL,
                        unitPrice INTEGER NOT NULL,
                        total INTEGER NOT NULL,
                        note TEXT NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS payments (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        orderId INTEGER NOT NULL,
                        amount INTEGER NOT NULL,
                        method TEXT NOT NULL,
                        paidAt INTEGER NOT NULL,
                        note TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }
    }
}
