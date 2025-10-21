package com.micro.events.appconfig.model

data class ApiRequest<T>(
    val userUUID: String,
    val data: T
)