package com.example.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.domain.model.BookModel
import com.example.presentation.book.BookItemGrid
import com.example.presentation.book.BookItemList
import com.example.presentation.search.SearchBar
import com.example.presentation.search.SearchDisplay

@Composable
fun MainScreen(
    onBookClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val searchBookList: LazyPagingItems<BookModel> = viewModel.searchBookList.collectAsLazyPagingItems()
    val newBookList = viewModel.newBookList.collectAsState().value
    // 책 리스트 종류: true -> list, false -> grid
    val listType by remember { mutableStateOf(true) }
    Column {
        Spacer(modifier = Modifier.statusBarsPadding())
        SearchBar(
            query = viewModel.searchState.query,
            onQueryChange = { viewModel.searchState.query = it },
            onSearch = {
                viewModel.searchBook(viewModel.searchState.query.text)
                viewModel.searchState.searching = true
                viewModel.searchState.searched = true
            },
            searchFocused = viewModel.searchState.focused || viewModel.searchState.query.text != "",
            onSearchFocusChange = {viewModel.searchState.focused = it},
            onClearQuery = {viewModel.searchState.query = TextFieldValue("") },
            searching = viewModel.searchState.searching,
        )
        searchBookList.apply {
            when (loadState.append) {
                is LoadState.Loading -> { viewModel.searchState.searching = false }
                is LoadState.NotLoading -> {}
                is LoadState.Error -> {}
            }
        }
        when (viewModel.searchState.searchDisplay) {
            SearchDisplay.StandBy -> {
                if(listType){
                    LazyColumn(
                        modifier = modifier,
                        contentPadding = PaddingValues(top = 6.dp, bottom = 6.dp),
                        userScrollEnabled = true
                    ) {
                        newBookList?.books?.size?.let {
                            items(it, key = {it}){ book ->
                                BookItemList(
                                    book = newBookList.books[book],
                                    onBookClick = onBookClick
                                )
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                    ){
                        newBookList?.books?.size?.let {
                            items(it, key = {it}){ book ->
                                BookItemGrid(
                                    book = newBookList.books[book],
                                    onBookClick = onBookClick
                                )
                            }
                        }
                    }
                }
            }
            SearchDisplay.Results -> {
                if(listType){
                    LazyColumn(
                        modifier = modifier,
                        contentPadding = PaddingValues(top = 6.dp, bottom = 6.dp),
                        userScrollEnabled = true
                    ) {
                        items(searchBookList.itemCount){
                            searchBookList.itemSnapshotList[it]?.let { it1 ->
                                BookItemList(
                                    book = it1,
                                    onBookClick = onBookClick
                                )
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                    ){
                        items(
                            count = searchBookList.itemCount
                        ){
                            searchBookList.itemSnapshotList[it]?.let { it1 ->
                                BookItemGrid(
                                    book = it1,
                                    onBookClick = onBookClick,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}