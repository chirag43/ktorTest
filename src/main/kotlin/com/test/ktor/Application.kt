package com.test.ktor

import com.test.ktor.dao.DatabaseFactory
import com.test.ktor.plugins.configureHTTP
import com.test.ktor.plugins.configureMonitoring
import com.test.ktor.plugins.configureRouting
import com.test.ktor.plugins.configureSecurity
import com.test.ktor.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
        .monitor.subscribe(ApplicationStopped) {
            DatabaseFactory.close()
        }

}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()

    DatabaseFactory.init()
}
