package com.example.data.mapper

import com.example.data.dto.NewBookData
import com.example.domain.model.BookModel
import com.example.domain.model.NewBookModel

object NewBookMapper {
    fun toDomain(data: NewBookData): NewBookModel{
        val bookList = mutableListOf<BookModel>()
        data.books.forEach {
            bookList.add(it.toDomain())
        }
        return NewBookModel(
            total = data.total,
            books = bookList
        )
    }
}
fun NewBookData.toDomain(): NewBookModel {
    return NewBookMapper.toDomain(this)
}

