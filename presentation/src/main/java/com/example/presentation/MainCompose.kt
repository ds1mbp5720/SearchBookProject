package com.example.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.domain.model.BookModel
import com.example.presentation.search.SearchBar
import com.example.presentation.search.SearchDisplay

@Composable
fun Search(
    onBookClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val searchBookList: LazyPagingItems<BookModel> = viewModel.searchBookList.collectAsLazyPagingItems()
    Column {
        Spacer(modifier = Modifier.statusBarsPadding())
        SearchBar(
            query = viewModel.searchState.query,
            onQueryChange = { viewModel.searchState.query = it },
            onSearch = {
                viewModel.searchBook(viewModel.searchState.query.text)
            },
            searchFocused = viewModel.searchState.focused || viewModel.searchState.query.text != "",
            onSearchFocusChange = {viewModel.searchState.focused = it},
            onClearQuery = {viewModel.searchState.query = TextFieldValue("") },
            searching = viewModel.searchState.searching,
        )
        searchBookList.apply {
            when{
                loadState.append is LoadState.Loading -> { viewModel.searchState.searching = false }
                loadState.append is LoadState.NotLoading -> {}
                loadState.append is LoadState.Error -> {}
                loadState.refresh is LoadState.Loading -> {}
                loadState.refresh is LoadState.Error -> {}
            }
        }
        when (viewModel.searchState.searchDisplay) {
            SearchDisplay.StandBy -> {

            }
            SearchDisplay.Results -> {

            }
        }
    }

}