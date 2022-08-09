package com.example.healthinfo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccessTokenEntity(
    val accessToken: String? = null,
    val email:String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int?= null
)