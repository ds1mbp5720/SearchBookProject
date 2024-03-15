package com.example.presentation.book.detil

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.MainViewModel

@Composable
fun DetailBookScreen(
    upPress: () -> Unit,
    viewModel: MainViewModel
){
    val detailBookInfo = viewModel.detailBook.collectAsStateWithLifecycle().value
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        if (detailBookInfo != null) {

        }
    }
}