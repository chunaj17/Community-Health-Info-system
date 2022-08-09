package com.example.healthinfo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.healthinfo.data.local.entity.AccessTokenEntity

@Dao
interface AccessTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccessToken(accessTokenEntity: AccessTokenEntity)

    @Query("SELECT * FROM accesstokenentity WHERE accessToken LIKE '%' || :accessToken || '%'")
    suspend fun getAccessToken(accessToken:String):List<AccessTokenEntity>

    @Query("DELETE FROM accesstokenentity WHERE accessToken IN(:accessToken)")
    suspend fun deleteAccessToken(accessToken: List<String>)

    @Query("SELECT * FROM accesstokenentity ")
    suspend fun checkAccessToken():List<AccessTokenEntity>

    @Query("DELETE FROM accesstokenentity")
    suspend fun logout()
}