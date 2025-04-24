package com.test.ktor.dao

import com.test.ktor.helper.EncryptionHelper.decrypt
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.HoconApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager

object DatabaseFactory {

    private lateinit var hikariDataSource: HikariDataSource

    private val appConfig = HoconApplicationConfig(ConfigFactory.load())

    private lateinit var fullDbUrl: String
    private lateinit var adminDbUrl : String
    private lateinit var dbName: String
    private lateinit var dbUser: String
    private lateinit var dbPassword: String

    fun init() {
        val dbPort = appConfig.property("db.port").getString()
        val dbHost = appConfig.property("db.host").getString()
        dbName = appConfig.property("db.name").getString()
        dbUser = appConfig.property("db.user").getString()
        val encryptedPassword = appConfig.property("db.password").getString()
        val encryptionKey = appConfig.property("db.encryptionKey").getString()

        dbPassword = decrypt(encryptedPassword, encryptionKey)

        fullDbUrl = "jdbc:mariadb://$dbHost:$dbPort/$dbName"
        adminDbUrl = "jdbc:mariadb://$dbHost:$dbPort/mysql"

        createDatabaseIfNotExists()

        hikariDataSource = HikariDataSource(HikariConfig().apply {
            jdbcUrl = fullDbUrl
            driverClassName = "org.mariadb.jdbc.Driver"
            username = dbUser
            password = dbPassword
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()

            // Optional HikariCP specific configurations
            minimumIdle = 2
            idleTimeout = 300000 // 5 minutes
            connectionTimeout = 20000 // 20 seconds
            maxLifetime = 1200000 // 20 minutes
        })

        Database.connect(hikariDataSource)

        transaction {
            SchemaUtils.create(UsersTable)
        }
    }

    private fun createDatabaseIfNotExists() {
        val connection = DriverManager.getConnection(adminDbUrl, dbUser, dbPassword)
        connection.use { conn ->
            val resultSet = conn.createStatement()
                .executeQuery("SHOW DATABASES LIKE '$dbName'")
            if (!resultSet.next()) {
                println("Database '$dbName' does not exist. Creating...")
                conn.createStatement().executeUpdate("CREATE DATABASE `$dbName`")
                println("Database '$dbName' created.")
            } else {
                println("Database '$dbName' already exists.")
            }
        }
    }


    fun close() {
        if (::hikariDataSource.isInitialized) {
            hikariDataSource.close()
        }
    }
}
