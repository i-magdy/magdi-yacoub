package org.myf.demo.core.network

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val data: T
)
