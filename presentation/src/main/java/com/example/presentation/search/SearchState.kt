package com.example.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
class SearchState(
    query: TextFieldValue,
    focused: Boolean,
    searching: Boolean,
    searched: Boolean
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching) // 검색 중 progress bar 표시
    var searched by mutableStateOf(searched) // 앱 시작 이후 검색 여부
    val searchDisplay: SearchDisplay
        get() = when { // 검색창의 현 상태값에 따른 보여줄 화면 정의를 위한 값 세팅부분
            !searched && !focused && query.text.isEmpty() -> SearchDisplay.StandBy
            !searched && focused -> SearchDisplay.StandBy
            else -> SearchDisplay.Results
        }
}

/**
 * 검색시 화면 구분용 class
 * StandBy: 검색 이전 api NewBook 결과 화면
 * Results: 검색 결과
  */
enum class SearchDisplay {
    Results, StandBy
}