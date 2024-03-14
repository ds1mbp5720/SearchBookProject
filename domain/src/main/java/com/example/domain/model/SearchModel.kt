package com.example.domain.model

data class SearchModel(
    val total: String,
    val page: String?,
    val books: List<BookModel>?
)