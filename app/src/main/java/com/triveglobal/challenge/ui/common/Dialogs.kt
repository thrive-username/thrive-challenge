package com.triveglobal.challenge.ui.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.triveglobal.challenge.R
import java.lang.Exception

@Composable
fun ErrorDialog(exception: Exception, onDismiss: () -> Unit, content: @Composable () -> Unit = { DefaultErrorMessage(exception) }) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(style = MaterialTheme.typography.h1, text = stringResource(id = R.string.dialog_error_title)) },
        text = { content() },
        confirmButton = {},
    )
}

@Composable
private fun DefaultErrorMessage(exception: Exception){
    Text(text = exception.message?:"Unknown error")
}