package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class NewBookData(
    @SerializedName("total")
    val total: String,
    @SerializedName("books")
    val books: List<BookData>
)
