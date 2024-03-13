package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.BookDetailModel
import com.example.domain.model.BookModel
import com.example.domain.model.NewBookModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun searchBook(query: String) : Flow<PagingData<BookModel>>

    fun getNewBookList() : Flow<NewBookModel>

    fun getDetailBook(isbn13: String) : Flow<BookDetailModel>
}