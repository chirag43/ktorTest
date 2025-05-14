package com.test.ktor

import com.test.ktor.dao.DatabaseFactory
import com.test.ktor.dao.UsersTable
import com.typesafe.config.ConfigFactory
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @BeforeTest
    fun setup() {
        // Load config from test resources
        val config = HoconApplicationConfig(ConfigFactory.load("application.conf"))
        DatabaseFactory.init(config)

        transaction {
            SchemaUtils.create(UsersTable)
            UsersTable.deleteAll()
            val sql = File("src/test/resources/test-data.sql").readText()
            sql.split(";")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .forEach { exec(it) }

        }
    }

    @AfterTest
    fun tearDown() {
        transaction {
            exec("DROP TABLE IF EXISTS users;")
        }
    }

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testGetUsers() = testApplication {
        application {
            module()
        }

        client.get("/users").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testGetUser() = testApplication {
        application {
            module()
        }

        val sampleUserId = "11111111-1111-1111-1111-111111111111"
        client.get("/users/$sampleUserId").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}