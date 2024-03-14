package com.example.presentation.book

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.domain.model.BookModel
import com.example.presentation.component.BookImage
import com.example.presentation.theme.BookSearchProjectTheme
import com.example.presentation.theme.BookSearchTheme

/**
 * list 형 책 component
 */
@Composable
fun BookItemList(
    book: BookModel,
    onBookClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BookSearchTheme.colors.uiBorder)
            .clickable { onBookClick(book.isbn13.toLong()) }
            .padding(horizontal = 16.dp)
    ) {
        val (image, title, price, subtitle) = createRefs()
        createVerticalChain(title, subtitle, chainStyle = ChainStyle.Packed)
        BookImage(
            imageUrl = book.image,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp,140.dp)
                .constrainAs(image) {
                    linkTo(
                        top = parent.top,
                        topMargin = 8.dp,
                        bottom = parent.bottom,
                        bottomMargin = 8.dp
                    )
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = BookSearchTheme.colors.textPrimary,
            modifier = Modifier
                .width(250.dp)
                .constrainAs(title) {
                    start.linkTo(image.end, 16.dp)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )
        Text(
            text = book.subtitle,
            style = MaterialTheme.typography.titleMedium,
            color = BookSearchTheme.colors.textPrimary,
            maxLines = 2,
            modifier = Modifier
                .width(250.dp)
                .constrainAs(subtitle) {
                    start.linkTo(image.end,16.dp)
                    bottom.linkTo(parent.bottom)
                }
        )
        Text(
            text = book.price,
            style = MaterialTheme.typography.bodyLarge,
            color = BookSearchTheme.colors.textLink,
            modifier = Modifier.constrainAs(price) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        )
    }
}


/**
 * Grid 형 책 component
 */
@Composable
fun BookItemGrid(
    book: BookModel,
    onBookClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onBookClick.invoke(book.isbn13.toLong()) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookImage(
            imageUrl = book.image,
            contentDescription = "book_image",
            modifier = Modifier
        )
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = BookSearchTheme.colors.textPrimary,
            modifier = Modifier
                .width(250.dp)
        )
        Text(
            text = book.subtitle,
            style = MaterialTheme.typography.titleMedium,
            color = BookSearchTheme.colors.textPrimary,
            maxLines = 2,
            modifier = Modifier
                .width(250.dp)
        )
        Text(
            text = book.price,
            style = MaterialTheme.typography.bodyLarge,
            color = BookSearchTheme.colors.textLink,
        )
    }
}