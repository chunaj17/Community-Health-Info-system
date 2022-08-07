package com.example.healthcare.ui.main

import com.example.healthcare.data.local.entity.AccessTokenEntity

data class TokenState(
    val accessTokenData:List<AccessTokenEntity>? = emptyList(),
    val isLoading:Boolean = false
)
