package com.example.data.mapper

import com.example.data.dto.SearchData
import com.example.domain.model.BookModel
import com.example.domain.model.SearchModel

object SearchMapper {
    fun toDomain(data: SearchData): SearchModel {
        val bookList = mutableListOf<BookModel>()
        data.books?.forEach {
            bookList.add(it.toDomain())
        }
        return SearchModel(
            total = data.total,
            page = data.page,
            books = bookList
        )
    }
}
fun SearchData.toDomain(): SearchModel {
    return SearchMapper.toDomain(this)
}