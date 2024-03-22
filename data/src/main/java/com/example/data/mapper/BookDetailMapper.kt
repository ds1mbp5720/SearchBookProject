package com.example.data.mapper

import com.example.data.dto.BookDetailData
import com.example.domain.model.BookDetailModel


object BookDetailMapper {
    fun toDomain(data: BookDetailData): BookDetailModel {
        return BookDetailModel(
            error = data.error,
            title = data.title,
            subtitle = data.subtitle,
            authors = data.authors,
            publisher = data.publisher,
            isbn10 = data.isbn10,
            isbn13 = data.isbn13,
            pages = data.pages,
            year = data.year,
            rating = data.rating,
            desc = data.desc,
            price = data.price,
            image = data.image,
            url = data.url,
            language = data.language
        )
    }
}

fun BookDetailData.toDomain(): BookDetailModel {
    return BookDetailMapper.toDomain(this)
}