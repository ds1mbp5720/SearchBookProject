package com.example.data.mapper

import com.example.data.dto.BookData
import com.example.domain.model.BookModel

object BookMapper {
    fun toDomain(data: BookData): BookModel {
        return BookModel(
            title = data.title,
            subtitle = data.subtitle,
            isbn13 = data.isbn13,
            price = data.price,
            image = data.image,
            url = data.url
        )
    }
}
fun BookData.toDomain(): BookModel {
    return BookMapper.toDomain(this)
}