package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class SearchData(
    @SerializedName("total")
    val total: String,
    @SerializedName("page")
    val page: String?,
    @SerializedName("books")
    val books: List<BookData>?
)
