package com.sotiemgiat.data.local.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: SoGiatDatabase? = null

    fun get(context: Context): SoGiatDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                SoGiatDatabase::class.java,
                "so_giat.db"
            ).build().also { INSTANCE = it }
        }
}
