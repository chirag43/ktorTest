package com.test.ktor.plugins

import com.test.ktor.models.Articles
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.i
import kotlinx.html.p

internal val LOG = KtorSimpleLogger("Routing")

fun Application.configureRouting() {
    install(Resources)
    routing {
//        get("/") {
//            call.respondText("Hello World!")
//        }
//        get("/help") {
//            call.respondText("Always there")
//        }

        get<Articles> { article ->
            // Get all articles ...
            call.respondText("List of articles sorted starting from ${article.sort}")
        }
        get<Articles.New> {
            // Show a page with fields for creating a new article ...
            call.respondText("Create a new article")
        }
        post<Articles> {
            // Save an article ...
            call.respondText("An article is saved", status = HttpStatusCode.Created)
        }
        get<Articles.Id> { article ->
            // Show an article with id ${article.id} ...
            call.respondText("An article with id ${article.id}", status = HttpStatusCode.OK)
        }
        get<Articles.Id.Edit> { article ->
            // Show a page with fields for editing an article ...
            call.respondText("Edit an article with id ${article.parent.id}", status = HttpStatusCode.OK)
        }
        put<Articles.Id> { article ->
            // Update an article ...
            call.respondText("An article with id ${article.id} updated", status = HttpStatusCode.OK)
        }
        delete<Articles.Id> { article ->
            // Delete an article ...
            call.respondText("An article with id ${article.id} deleted", status = HttpStatusCode.OK)
        }
        // Building links from resources
        get {
            call.respondHtml {
                body {
                    this@configureRouting.apply {
                        p {
                            val link: String = href(Articles())
                            a(link) { +"Get all articles" }
                        }
                        p {
                            val link: String = href(Articles.New())
                            a(link) { +"Create a new article" }
                        }
                        p {
                            val link: String = href(Articles.Id.Edit(Articles.Id(id = 123)))
                            a(link) { +"Edit an exising article" }
                        }
                        p {
                            val urlBuilder = URLBuilder(URLProtocol.HTTPS, "ktor.io", parameters = parametersOf("token", "123"))
                            href(Articles(sort = null), urlBuilder)
                            val link: String = urlBuilder.buildString()
                            i { a(link) { +link } }
                        }
                    }
                }
            }
        }
//        get<Articles> { articleReq ->
//            // Get all articles ...
////            log("Got the request as $articleReq")
//            LOG.info("Got the request as ${articleReq.search}")
//
//            val myArticle = Article("Sample", "CPatel", Instant.now().epochSecond)
//            LOG.info("Returning response as $myArticle")
//            call.respond(listOf(myArticle))
////            call.respond("List of articles sorted starting from ${article.sort}")
//        }
    }
}

