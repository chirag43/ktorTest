package com.test.ktor.models

@kotlinx.serialization.Serializable
data class ErrorResponse(
    val status: String,
    val message: String
)

@kotlinx.serialization.Serializable
data class NewUserRequest(
    val name: String,
    val password: String
)

@kotlinx.serialization.Serializable
data class UserResponse(
    val id: String,
    val name: String
)
