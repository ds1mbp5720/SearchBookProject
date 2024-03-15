package com.example.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.presentation.book.detil.DetailBookScreen
import com.example.presentation.navigation.BookSearchDestination
import com.example.presentation.navigation.rememberBookSearchNavController
import com.example.presentation.theme.BookSearchProjectTheme

@Composable
fun BookSearchAppCompose(){
    val bookSearchNavController = rememberBookSearchNavController()
    BookSearchProjectTheme{
        val mainViewModel: MainViewModel = viewModel()
        NavHost(
            navController = bookSearchNavController.navController,
            startDestination = BookSearchDestination.MAIN
        ){
            composable(BookSearchDestination.MAIN) { from ->
                MainScreen(onBookClick = {id -> bookSearchNavController.navigateToBookDetail(id, from)}, viewModel = mainViewModel)
            }
            composable("${BookSearchDestination.DETAIL}/{${BookSearchDestination.BOOK_ID}}",
                arguments = listOf(navArgument(BookSearchDestination.BOOK_ID) { type = NavType.LongType })
            ) {navBackStackEntry ->
                val arguments = requireNotNull(navBackStackEntry.arguments)
                val bookId = arguments.getLong(BookSearchDestination.BOOK_ID)
                mainViewModel.getDetailBook(bookId.toString())
                DetailBookScreen(upPress = {bookSearchNavController.upPress()}, viewModel = mainViewModel)
            }
        }
    }
}