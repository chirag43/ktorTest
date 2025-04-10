package com.test.ktor.models

import java.time.Instant

enum class UserRole {
    ADMIN,
    SUPER_ADMIN,
    USER,
    TESTER,
}

data class User(
    val name: String,
    val displayName: String = name,
    val role: UserRole = UserRole.USER,
    val lastLoginTime: Instant? = null,
)