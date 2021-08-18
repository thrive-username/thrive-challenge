package com.triveglobal.challenge.model

data class ResourceState<T>(
    val data: T? = null,
    val error: Exception? = null,
    val loading: Boolean = false
)