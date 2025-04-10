package com.test.ktor

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import com.test.ktor.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun testHelp() = testApplication {
        application {
            configureRouting()
        }
        client.get("/help").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Always there", bodyAsText())
        }
    }
}
