package com.example.domain.model

data class BookDetailModel(
    val error: String,
    val title: String, // 제목
    val subtitle: String, // 부제목
    val authors: String, // 저자
    val publisher: String, // 출판사
    val isbn10: String, //
    val isbn13: String, //
    val pages: String, // 페이지
    val year: String, // 출판 년도
    val rating: String, // 별점
    val desc: String, // 설명
    val price: String, // 가격(달러)
    val image: String, // 사진
    val url: String, // 링크
    val language: String // 언어
)