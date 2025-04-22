package com.test.ktor.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager

object DatabaseFactory {

    private lateinit var hikariDataSource: HikariDataSource
    private const val dbName = "ktorTest"
    private const val dbPort = "3306"
    private const val dbHost = "localhost"
    private const val dbUser = "test"
    private const val dbPassword = "password"

    private const val fullDbUrl = "jdbc:mariadb://$dbHost:$dbPort/$dbName"
    private const val adminDbUrl = "jdbc:mariadb://$dbHost:$dbPort/mysql"



    fun init() {
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
