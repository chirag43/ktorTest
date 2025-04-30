package com.test.ktor.layouts

import kotlinx.html.BODY
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.footer
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.nav
import kotlinx.html.style
import kotlinx.html.title

fun FlowContent.navbar() {
    nav {
        style = "background-color:#222; padding:1rem; color:white;"
        a("/") {
            style = "color:white; margin-right:1rem;"
            +"Home"
        }
        a("/users") {
            style = "color:white;"
            +"Users"
        }
    }
}

fun HTML.layout(titleText: String, content: BODY.() -> Unit) {
    head {
        title { +titleText }
        link(rel = "stylesheet", href = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css")
    }
    body {
        navbar()
        div("container mt-4") {
            content(this@body)
        }
        footer {
            style = "margin-top: 2rem; text-align: center; font-size: 0.9em;"
            +"© 2025 KtorTestApp"
        }
    }
}