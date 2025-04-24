package com.test.ktor.plugins

import com.test.ktor.dao.UsersTable
import com.test.ktor.models.ErrorResponse
import com.test.ktor.models.NewUserRequest
import com.test.ktor.models.UserResponse
import com.test.ktor.models.Users
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.request.receive
import io.ktor.server.resources.Resources
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.logging.KtorSimpleLogger
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

internal val LOG = KtorSimpleLogger("Routing")

fun Application.configureRouting() {
    install(Resources)
    routing {

        get<Users> {
            val page = 1
            val size = 10
            val users: List<UserResponse> = transaction {
                UsersTable.selectAll()
                    .limit(size)
                    .offset(start = (page-1)*size.toLong())
                    .map {
                        UserResponse(
                            id = it[UsersTable.id],
                            name = it[UsersTable.userName] ?: "unknown"
                        )
                    }
            }
            call.respond(users)
        }

        get<Users.ById> { userById ->
            val userId: Int = userById.id
            val user: ResultRow? = transaction {
                UsersTable.selectAll().where { UsersTable.id eq userId }.singleOrNull()
            }
            user?.let {
                val userResponse = UserResponse(
                    id = it[UsersTable.id],
                    name = it[UsersTable.userName] ?: "unknown"
                )
                call.respond(userResponse)
            } ?: run {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Not Found", "User not found with ID = $userId"))

            }
        }

        post("/users/add") {
            val newUser = call.receive<NewUserRequest>()

            if (newUser.name.isBlank() || newUser.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Username and password must not be blank.")
                return@post
            }

            val insertedId = transaction {
                UsersTable.insert {
                    it[userName] = newUser.name
                    it[password] = newUser.password
                } get UsersTable.id
            }

            call.respond(HttpStatusCode.Created, "User created with ID = $insertedId")
        }

        delete<Users.ById> { userById ->
            val userId: Int = userById.id
            val deletedCount = transaction {
                UsersTable.deleteWhere { UsersTable.id eq userId }
            }
            if (deletedCount > 0) {
                call.respondText("User $userId deleted", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("User $userId not found", status = HttpStatusCode.NotFound)
            }
        }

//        get<Articles> { article ->
//            // Get all articles ...
//            call.respondText("List of articles sorted starting from ${article.sort}")
//        }
//        get<Articles.New> {
//            // Show a page with fields for creating a new article ...
//            call.respondText("Create a new article")
//        }
//        post<Articles> {
//            // Save an article ...
//            call.respondText("An article is saved", status = HttpStatusCode.Created)
//        }
//        get<Articles.Id> { article ->
//            // Show an article with id ${article.id} ...
//            call.respondText("An article with id ${article.id}", status = HttpStatusCode.OK)
//        }
//        get<Articles.Id.Edit> { article ->
//            // Show a page with fields for editing an article ...
//            call.respondText("Edit an article with id ${article.parent.id}", status = HttpStatusCode.OK)
//        }
//        put<Articles.Id> { article ->
//            // Update an article ...
//            call.respondText("An article with id ${article.id} updated", status = HttpStatusCode.OK)
//        }
//        delete<Articles.Id> { article ->
//            // Delete an article ...
//            call.respondText("An article with id ${article.id} deleted", status = HttpStatusCode.OK)
//        }

        // Building links from resources
//        get {
//            call.respondHtml {
//                body {
//                    this@configureRouting.apply {
//                        p {
//                            val link: String = href("/users")
//                            a(link) { +"Get all users" }
//                        }
//                        p {
//                            val link: String = href("/users/1")
//                            a(link) { +"Get first user" }
//                        }
//                        p {
//                            val link: String = href("/users/add")
//                            a(link) { +"Add a new user" }
//                        }
//                        p {
//                            val urlBuilder =
//                                URLBuilder(URLProtocol.HTTPS, "ktor.io", parameters = parametersOf("token", "123"))
//                            href(Articles(sort = null), urlBuilder)
//                            val link: String = urlBuilder.buildString()
//                            i { a(link) { +link } }
//                        }
//                    }
//                }
//            }
//        }
    }
}

