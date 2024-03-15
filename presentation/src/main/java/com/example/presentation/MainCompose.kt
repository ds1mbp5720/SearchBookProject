package com.example.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
import com.example.presentation.theme.BookSearchTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    onBookClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    val searchBookList: LazyPagingItems<BookModel> = viewModel.searchBookList.collectAsLazyPagingItems()
    val newBookList = viewModel.newBookList.collectAsState().value
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    // 책 리스트 종류: true -> list, false -> grid
    var listType by remember { mutableStateOf(false) }
    Column {
        Spacer(modifier = Modifier.statusBarsPadding())
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
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
                listChange = {
                    listType = !listType
                    coroutine.launch {
                        listState.scrollToItem(0)
                        gridState.scrollToItem(0)
                    }
                }
            )
        }
        // 검색 결과 flow 동작 상태
        LaunchedEffect(key1 = searchBookList.loadState ){
            searchBookList.apply {
                when {
                    loadState.append is LoadState.Loading -> {
                        viewModel.searchState.searching = false
                        viewModel.searchState.noResult = false
                    }
                    loadState.append is LoadState.NotLoading -> {
                         if(this.loadState.append.endOfPaginationReached){ // 검색 결과 없는 경우
                             Toast.makeText(context, context.getString(R.string.str_no_result), Toast.LENGTH_SHORT).show()
                             viewModel.searchState.searching = false
                             viewModel.searchState.noResult = true
                         }
                    }
                    loadState.append is LoadState.Error -> {
                        Toast.makeText(context, "Error:" + (loadState.append as LoadState.Error).error.message, Toast.LENGTH_SHORT).show()
                    }
                    loadState.refresh is LoadState.Error -> {
                        Toast.makeText(context, "Error:" + (loadState.refresh as LoadState.Error).error.message, Toast.LENGTH_SHORT).show()
                    }
                }
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
                            items(it, key = {newBookList.books[it].isbn13}){ book ->
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
                            items(it, key = {newBookList.books[it].isbn13}){ book ->
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
                if(viewModel.searchState.noResult){
                    Text(
                        text = stringResource(id = R.string.str_no_result),
                        style = MaterialTheme.typography.titleLarge,
                        color = BookSearchTheme.colors.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    )
                } else {
                    if(listType){
                        LazyColumn(
                            modifier = modifier,
                            contentPadding = PaddingValues(top = 6.dp, bottom = 6.dp),
                            state = listState
                        ) {
                            items(
                                searchBookList.itemCount,
                                key = {
                                    searchBookList.peek(it)?.isbn13 ?: it
                                },
                            ) {
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
                            modifier = Modifier,
                            state = gridState
                        ){
                            items(
                                count = searchBookList.itemCount,
                                key = {
                                    searchBookList.peek(it)?.isbn13 ?: 0
                                }
                            ) {
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
}