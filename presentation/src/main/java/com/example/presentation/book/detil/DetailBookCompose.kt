package com.example.presentation.book.detil

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.BookDetailModel
import com.example.presentation.MainViewModel
import com.example.presentation.R
import com.example.presentation.component.BookImage
import com.example.presentation.component.MainSurface
import com.example.presentation.theme.BookSearchTheme
import com.example.presentation.theme.Neutral5
import com.example.presentation.utils.mirroringBackIcon

private val titleHeight = 160.dp
private val gradientScroll = 180.dp
private val imageOverlap = 115.dp
private val minTitleOffset = 56.dp
private val minImageOffset = 12.dp
private val maxTitleOffset = imageOverlap + minTitleOffset + gradientScroll
private val expandedImageSize = 300.dp
private val collapsedImageSize = 90.dp
private val horizontalPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun DetailBookScreen(
    upPress: () -> Unit,
    viewModel: MainViewModel
){
    val detailBookInfo = viewModel.detailBook.collectAsStateWithLifecycle().value
    val scroll = rememberScrollState(0)
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        IconButton(
            onClick = upPress,
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .size(36.dp)
                .background(
                    color = Neutral5,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = mirroringBackIcon(),
                tint = BookSearchTheme.colors.iconInteractive,
                contentDescription = "back_main"
            )
        }
        if (detailBookInfo != null) {
            Body(book = detailBookInfo, scroll = scroll)
            Title(
                book = detailBookInfo,
                scrollProvider = { scroll.value }
            )
            Image(imageUrl = detailBookInfo.image, scrollProvider = { scroll.value })
        }
    }
}
@Composable
private fun Image(
    imageUrl: String,
    scrollProvider: () -> Int
){
    val collapseRange = with(LocalDensity.current) { (maxTitleOffset - minTitleOffset).toPx()}
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }
    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = horizontalPadding.then(Modifier.statusBarsPadding())
    ){
        BookImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }

}
@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Layout(
        modifier = modifier,
        content = content
    ){ measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = Integer.min(expandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = Integer.max(collapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(minTitleOffset, minImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageMaxSize) / 2,
            (constraints.maxWidth - imageMinSize) / 2,
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ){
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}
@Composable
private fun Title(
    book: BookDetailModel,
    scrollProvider: () -> Int,
){
    val maxOffset = with(LocalDensity.current) { maxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { (collapsedImageSize).toPx() }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = titleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(color = BookSearchTheme.colors.uiBackground)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Column{
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                color = BookSearchTheme.colors.textPrimary,
                modifier = horizontalPadding
            )
            Text(
                text = book.subtitle,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                color = BookSearchTheme.colors.textPrimary,
                modifier = horizontalPadding
            )
            Text(
                text = book.authors,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                color = BookSearchTheme.colors.textPrimary,
                modifier = horizontalPadding
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = book.price,
            style = MaterialTheme.typography.displayMedium,
            color = BookSearchTheme.colors.textPrimary,
            modifier = horizontalPadding
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = BookSearchTheme.colors.uiBorder)
    }
}

@Composable
private fun Body(
    book: BookDetailModel,
    scroll: ScrollState
){
    val context = LocalContext.current
    Column {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(minTitleOffset))
        Spacer(modifier = Modifier.height(imageOverlap))
        Spacer(modifier = Modifier.height(titleHeight))
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(modifier = Modifier.height(gradientScroll))
            MainSurface(Modifier.fillMaxWidth()) {
                Column{
                    Text(
                        text = stringResource(id = R.string.str_detail_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = BookSearchTheme.colors.textHelp,
                        modifier = horizontalPadding
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = book.desc,
                        color = BookSearchTheme.colors.textPrimary,
                        maxLines = if(seeMore) 2 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = horizontalPadding
                    )
                    val textButton =
                        if(seeMore) stringResource(id = R.string.str_see_more)
                        else stringResource(id = R.string.str_see_less)
                    Text(
                        text = textButton,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BookSearchTheme.colors.textLink,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp, start = 40.dp)
                            .clickable { seeMore = !seeMore }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(id = R.string.str_link),
                        style = MaterialTheme.typography.titleLarge,
                        color = BookSearchTheme.colors.textHelp,
                        modifier = horizontalPadding
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = book.url,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BookSearchTheme.colors.textLink,
                        modifier = horizontalPadding
                            .clickable {
                                context.startActivity( Intent(Intent.ACTION_VIEW, Uri.parse(book.url)) )
                            }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(id = R.string.str_publisher),
                        style = MaterialTheme.typography.titleLarge,
                        color = BookSearchTheme.colors.textHelp,
                        modifier = horizontalPadding
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = book.publisher,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BookSearchTheme.colors.textHelp,
                        modifier = horizontalPadding
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ){
                        BodySimpleInfo(title = stringResource(id = R.string.str_page), detail = book.pages)
                        BodySimpleInfo(title = stringResource(id = R.string.str_language), detail = book.language)
                        BodySimpleInfo(title = stringResource(id = R.string.str_rating), detail = book.rating)
                        BodySimpleInfo(title = stringResource(id = R.string.str_year), detail = book.year)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun BodySimpleInfo(title: String, detail: String){
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = BookSearchTheme.colors.textHelp,
            modifier = horizontalPadding
        )
        Text(
            text = detail,
            style = MaterialTheme.typography.bodyLarge,
            color = BookSearchTheme.colors.textHelp,
            modifier = horizontalPadding
        )
    }
}