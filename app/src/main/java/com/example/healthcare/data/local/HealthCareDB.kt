package com.example.healthcare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthcare.data.local.entity.AccessTokenEntity

@Database(
    entities = [AccessTokenEntity::class],
    version = 1,
    exportSchema = true
)
abstract class HealthCareDB:RoomDatabase() {
    abstract val dao:AccessTokenDao
}