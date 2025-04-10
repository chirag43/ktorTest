package com.test.ktor.plugins

import com.test.ktor.models.Article
import com.test.ktor.models.ArticlesReq
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.application.*
import io.ktor.util.logging.*
import java.time.Instant

internal val LOG = KtorSimpleLogger("Routing")

fun Application.configureRouting() {
    install(Resources)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/help") {
            call.respondText("Always there")
        }

        get<ArticlesReq> { articleReq ->
            // Get all articles ...
//            log("Got the request as $articleReq")
            LOG.info("Got the request as ${articleReq.search}")

            val myArticle = Article("Sample", "CPatel", Instant.now().epochSecond)
            LOG.info("Returning response as $myArticle")
            call.respond(listOf(myArticle))
//            call.respond("List of articles sorted starting from ${article.sort}")
        }
    }
}

