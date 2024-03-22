package com.example.presentation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.domain.model.BookModel
import com.example.presentation.book.BookItemGrid
import com.example.presentation.book.BookItemList
import com.example.presentation.component.BaseDialog
import com.example.presentation.search.SearchBar
import com.example.presentation.search.SearchDisplay
import com.example.presentation.theme.BookSearchTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    onBookClick: (Long) -> Unit, modifier: Modifier = Modifier, viewModel: MainViewModel
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val searchBookList: LazyPagingItems<BookModel> = viewModel.searchBookList.collectAsLazyPagingItems()
    val newBookList = viewModel.newBookList.collectAsState().value
    val loadingState = viewModel.loading.collectAsState()
    var listType by remember { mutableStateOf(false) } // 책 리스트 종류: true -> list, false -> grid
    var refreshing by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) } // 종료 확인 Dialog 노출 상태
    val scrollState = rememberPullRefreshState(refreshing = refreshing, onRefresh = {
        // 현재 화면 상태에 따른 api 호출 종류 분할
        when (viewModel.searchState.searchDisplay) {
            SearchDisplay.Results -> {
                refreshing = true
                viewModel.searchBook(viewModel.searchState.query.text)
            }

            SearchDisplay.StandBy -> {
                refreshing = true
                viewModel.getNewBookList()
            }
        }
    })
    LaunchedEffect(key1 = loadingState.value) {
        refreshing = loadingState.value
    }
    // 검색 결과 flow 동작 상태
    LaunchedEffect(key1 = searchBookList.loadState) {
        searchBookList.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    viewModel.searchState.searching = false
                    viewModel.searchState.noResult = false
                    refreshing = false
                }

                loadState.append is LoadState.NotLoading -> {
                    if (this.loadState.append.endOfPaginationReached && this.itemCount == 0) { // 검색 결과 없는 경우
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
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.statusBarsPadding())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            SearchBar(
                query = viewModel.searchState.query,
                onQueryChange = {
                    viewModel.searchState.query = it
                    if (viewModel.searchState.query.text.isEmpty()) viewModel.searchState.searched = false
                },
                onSearch = {
                    viewModel.searchBook(viewModel.searchState.query.text)
                    viewModel.searchState.searching = true
                    viewModel.searchState.searched = true
                },
                searchFocused = viewModel.searchState.focused || viewModel.searchState.query.text != "",
                onSearchFocusChange = { viewModel.searchState.focused = it },
                onClearQuery = {
                    viewModel.searchState.query = TextFieldValue("")
                    viewModel.searchState.searched = false
                },
                searching = viewModel.searchState.searching
            )
            IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = {
                listType = !listType
            }) {
                Icon(
                    imageVector = Icons.Outlined.List, tint = MaterialTheme.colorScheme.primary, contentDescription = "list_type"
                )
            }
        }
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(
                    if (refreshing) 90.dp
                    else lerp(0.dp, 90.dp, scrollState.progress.coerceIn(0f..1f))
                )
        ) {
            if (refreshing) {
                CircularProgressIndicator(
                    color = BookSearchTheme.colors.textPrimary, modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .align(Alignment.Center)
                        .size(70.dp)
                )
            }
        }
        when (viewModel.searchState.searchDisplay) {
            SearchDisplay.StandBy -> {
                if (newBookList != null && !refreshing) {
                    BasicBookListBody(
                        modifier = Modifier.pullRefresh(scrollState),
                        listType = listType,
                        newBookList = newBookList.books,
                        onBookClick = { bookId ->
                            viewModel.getDetailBook(bookId.toString())
                            onBookClick.invoke(bookId)
                        }
                    )
                }
            }

            SearchDisplay.Results -> {
                if (viewModel.searchState.noResult) {
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
                    if (!refreshing) {
                        BasicBookListBody(
                            modifier = Modifier.pullRefresh(scrollState),
                            listType = listType,
                            searchBookList = searchBookList,
                            onBookClick = { bookId ->
                                viewModel.getDetailBook(bookId.toString())
                                onBookClick.invoke(bookId)
                            }
                        )
                    }
                }
            }
        }
    }
    BackHandler(
        enabled = true
    ) {
        showDialog.value = true
    }
    if (showDialog.value) {
        BaseDialog(title = stringResource(id = R.string.str_app_exit_title), dismissAction = {
            showDialog.value = false
        }, confirmAction = {
            showDialog.value = true
            activity.finish()
        })
    }
}

/**
 * 책 리스트 compose
 * new 와 search 의 타입이 List<> 와 LazyPagingItems 로 달라서
 * 두개의 타입을 기본으로 null로 가지고있고 null이 아닌 타입에 맞춰 표시
 */
@Composable
fun BasicBookListBody(
    modifier: Modifier,
    listType: Boolean,
    newBookList: List<BookModel>? = null,
    searchBookList: LazyPagingItems<BookModel>? = null,
    onBookClick: (Long) -> Unit
) {
    val newType = newBookList != null
    val bookItemCount = if (newType) newBookList!!.size else searchBookList!!.itemCount
    if (listType) {
        LazyColumn(
            modifier = modifier, contentPadding = PaddingValues(top = 6.dp, bottom = 6.dp)
        ) {
            items(
                count = bookItemCount,
                key = {
                    if (newType) {
                        newBookList!![it].isbn13
                    } else {
                        searchBookList!!.peek(it)!!.isbn13
                    }
                }
            ) { book ->
                (if (newType) {
                    newBookList!![book]
                } else {
                    searchBookList!![book]
                })?.let {
                    BookItemList(
                        book = it,
                        onBookClick = onBookClick
                    )
                }
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), modifier = modifier
        ) {
            items(
                count = bookItemCount,
                key = {
                    if (newType) {
                        newBookList!![it].isbn13
                    } else {
                        searchBookList!!.peek(it)!!.isbn13
                    }
                }
            ) { book ->
                (if (newType) {
                    newBookList!![book]
                } else {
                    searchBookList!![book]
                })?.let {
                    BookItemGrid(
                        book = it,
                        onBookClick = onBookClick
                    )
                }
            }
        }
    }
}