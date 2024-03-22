package com.example.data.datasource

import com.example.data.dto.BookDetailData
import com.example.data.dto.NewBookData
import com.example.data.dto.SearchData
import retrofit2.http.GET
import retrofit2.http.Path

interface BookDateSource {

    @GET("search/{query}/{page}")
    suspend fun searchBook(
        @Path("query") query: String,
        @Path("page") page: String
    ): SearchData

    @GET("new")
    suspend fun getNewBookList(): NewBookData

    @GET("books/{isbn13}")
    suspend fun getDetailBook(
        @Path("isbn13") isbn13: String
    ): BookDetailData
}