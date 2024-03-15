package com.example.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object BookSearchDestination {
    const val MAIN = "main"
    const val DETAIL = "detail"
    const val BOOK_ID = "BookId"
}

@Composable
fun rememberBookSearchNavController(
    navController: NavHostController = rememberNavController()
): BookSearchNavController = remember(navController) {
    BookSearchNavController(navController)
}

@Stable
class BookSearchNavController(
  val navController: NavHostController
) {
    fun upPress() { navController.navigateUp() }

    fun navigateToBookDetail(bookId: Long, from: NavBackStackEntry) {
        if(from.lifeCycleIsResume()){
            navController.navigate("${BookSearchDestination.DETAIL}/$bookId")
        }
    }
}
private fun NavBackStackEntry.lifeCycleIsResume() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED