package com.example.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.theme.BookSearchTheme

/**
 * 기본형 Dialog
 * Column 버튼 2개
 */
@Composable
fun BaseDialog(
    title: String = "",
    body: String = "",
    dismissAction: () -> Unit,
    confirmAction: () -> Unit
){
    AlertDialog(
        containerColor = BookSearchTheme.colors.uiBackground,
        onDismissRequest = dismissAction,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = BookSearchTheme.colors.textPrimary,)
        },
        text = {
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = BookSearchTheme.colors.textPrimary,
            )
        },
        dismissButton = {
            Button(
                onClick = dismissAction,
                modifier = Modifier,
                border = BorderStroke(width = 1.dp, color = Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = BookSearchTheme.colors.brandSecondary)
            ) {
                Text(
                    text = stringResource(id = R.string.str_cancel),
                    modifier = Modifier.fillMaxWidth(),
                    color = BookSearchTheme.colors.textPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = confirmAction,
                modifier = Modifier,
                border = BorderStroke(width = 1.dp, color = Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = BookSearchTheme.colors.brandSecondary)
            ) {
                Text(
                    text = stringResource(id = R.string.str_confirm),
                    modifier = Modifier.fillMaxWidth(),
                    color = BookSearchTheme.colors.textPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}