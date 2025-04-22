package com.test.ktor.dao

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UsersTable : Table(name = "users") {
    val id: Column<Int> = integer("id").autoIncrement()
    val userName: Column<String?> = varchar("username", 45).nullable().default(null)
    val password: Column<String?> = varchar("password", 50).nullable().default(null)

    override val primaryKey = PrimaryKey(id)
}