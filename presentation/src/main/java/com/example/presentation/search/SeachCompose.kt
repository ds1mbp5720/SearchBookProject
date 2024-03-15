package com.example.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.component.MainSurface
import com.example.presentation.theme.BookSearchTheme
import com.example.presentation.utils.mirroringBackIcon

/**
 * 상단 검색어 Bar
 *     query: 입력 전 검색어
 *     onQueryChange: 입력 후 검색어 (입력에 따른 값 갱신)
 *     onSearch: () -> Unit,  검색 api 호출
 *     searchFocused, 검색 동작 여부
 *     onSearchFocusChange: (Boolean) -> Unit,
 *     onClearQuery: () -> Unit, 검색어 지우기
 *     searching: Boolean, 검색중 progress 동작용 변수
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearch: () -> Unit, // 검색 api 호출
    searchFocused: Boolean,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    searching: Boolean,
    modifier: Modifier = Modifier
){
    val keyboardController = LocalSoftwareKeyboardController.current // 특정 행동들 이후 키보드 visible 조정을 위한 controller
    val focusManager = LocalFocusManager.current // EditText focus 조정
    MainSurface(
        modifier = modifier
            .padding(horizontal = 48.dp, vertical = 8.dp)
    ){
        Box(modifier = Modifier.fillMaxSize()){
            if(query.text.isEmpty()) SearchHint(Modifier.align(Alignment.Center))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentHeight()
            ){
                if(searchFocused) {
                    IconButton(onClick = onClearQuery) {
                        Icon(
                            imageVector = mirroringBackIcon(),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "ic_back"
                        )
                    }
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch.invoke()
                            keyboardController?.hide()
                            focusManager.clearFocus(true)
                        }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            onSearchFocusChange(it.isFocused)
                        }
                )
                if(searching){
                    CircularProgressIndicator(
                        color = BookSearchTheme.colors.textPrimary,
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(36.dp)
                    )
                } else{
                    Spacer(modifier = Modifier.width(48.dp))
                }
                if(searchFocused) {
                    IconButton(
                        onClick = {
                            onSearch.invoke()
                            keyboardController?.hide()
                            focusManager.clearFocus(true)
                            onSearchFocusChange(false)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            tint = BookSearchTheme.colors.iconSecondary,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}

/**
 * 검색어 입력 전 간단한 표시 view
 */
@Composable
fun SearchHint(modifier: Modifier){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.wrapContentSize()
    ){
        Icon(
            imageVector = Icons.Outlined.Search,
            tint = BookSearchTheme.colors.iconSecondary,
            contentDescription = "ic_search",
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.str_search_hint),
            color = BookSearchTheme.colors.textHelp
        )
    }
}