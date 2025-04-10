package com.test.ktor.models

import io.ktor.resources.*
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

@Resource("/users")
class Users {
    @Resource("/{id}")
    class ById(val parent: Users = Users(), val id: Long)

    @Resource("/add")
    class Add(val parent: Users = Users(), val name: String)

}
