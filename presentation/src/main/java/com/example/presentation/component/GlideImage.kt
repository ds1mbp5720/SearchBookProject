package com.example.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.presentation.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookImage(
    imageUrl: String,
    contentDescription : String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    MainSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = RectangleShape,
        modifier = modifier,
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ){
        GlideImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.wrapContentSize(),
        ){
            it.error(R.drawable.baseline_menu_book_24)
                .placeholder(R.drawable.baseline_menu_book_24)
                .load(imageUrl)
        }
    }
}