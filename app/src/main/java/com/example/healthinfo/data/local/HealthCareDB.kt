package com.example.healthinfo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthinfo.data.local.entity.AccessTokenEntity

@Database(
    entities = [AccessTokenEntity::class],
    version = 3,
    exportSchema = true
)
abstract class HealthCareDB:RoomDatabase() {
    abstract val dao:AccessTokenDao
}