package com.example.presentation

import android.app.Application
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.BookDetailModel
import com.example.domain.model.BookModel
import com.example.domain.model.NewBookModel
import com.example.domain.repository.BookRepository
import com.example.presentation.search.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * api 통신 및 상태관리 MainViewModel
 * searchState: 검색 입력 상태 관리 변수
 * loading: api 호출 후 상태 관리 -> true: onStart, collect: false
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    application: Application
) : AndroidViewModel(application) {
    // 상태에 맞춰 상단 검색 바 갱신
    val searchState: SearchState = SearchState(query = TextFieldValue(""), focused = false, searching = false, searched = false, noResult = false)

    // api 통신 진행 표시
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _searchBookList: MutableStateFlow<PagingData<BookModel>> = MutableStateFlow(value = PagingData.empty())
    val searchBookList: StateFlow<PagingData<BookModel>> = _searchBookList.asStateFlow()
    fun searchBook(query: String) {
        viewModelScope.launch {
            bookRepository.searchBook(query = query)
                .cachedIn(viewModelScope)
                //.distinctUntilChanged()
                .collectLatest {
                    _searchBookList.value = it
                }
        }
    }

    private val _newBookList = MutableStateFlow<NewBookModel?>(null)
    val newBookList: StateFlow<NewBookModel?> = _newBookList.asStateFlow()
    fun getNewBookList() {
        viewModelScope.launch {
            bookRepository.getNewBookList()
                .onStart { _loading.value = true }
                .collectLatest {
                    _loading.value = false
                    _newBookList.value = it
                }
        }
    }

    private val _detailBook = MutableStateFlow<BookDetailModel?>(null)
    val detailBook: StateFlow<BookDetailModel?> = _detailBook.asStateFlow()
    fun getDetailBook(isbn13: String) {
        viewModelScope.launch {
            bookRepository.getDetailBook(isbn13)
                .collectLatest {
                    _detailBook.value = it
                }
        }
    }
}