package com.example.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
class SearchState(
    query: TextFieldValue,
    focused: Boolean,
    searching: Boolean
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching)
    val searchDisplay: SearchDisplay
        get() = when {
            // 검색창의 현 상태값에 따른 보여줄 화면 정의를 위한 값 세팅부분
            !focused && query.text.isEmpty() -> SearchDisplay.StandBy
            focused && query.text.isEmpty() -> SearchDisplay.StandBy
            else -> SearchDisplay.Results
        }
}

enum class SearchDisplay {
    Results, StandBy
}