package com.example.healthinfo.ui.main

import com.example.healthinfo.data.local.entity.AccessTokenEntity

data class TokenState(
    val accessTokenData:List<AccessTokenEntity>? = emptyList(),
    val isLoading:Boolean = false
)
