package com.test.ktor.models

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
@Resource("/articles")
class ArticlesReq(val sort: String? = "new", val search: String? = null)

data class Article(val name: String, val author:String, val publishDate: Long)
