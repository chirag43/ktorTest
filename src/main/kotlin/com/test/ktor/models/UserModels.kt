package com.test.ktor.models

import io.ktor.resources.Resource


enum class UserRole {
    ADMIN,
    SUPER_ADMIN,
    USER,
    TESTER,
}

@Resource("/users")
class Users {
    @Resource("/{id}")
    data class ById(val parent: Users = Users(), val id: Int)

//    @Resource("/add")
//    class Add(val parent: Users = Users(), val name: String, val password: String)

}
