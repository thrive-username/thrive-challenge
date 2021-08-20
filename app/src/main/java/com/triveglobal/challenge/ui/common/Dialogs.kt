package com.triveglobal.challenge.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.triveglobal.challenge.R
import com.triveglobal.challenge.ui.theme.White
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

@Preview
@Composable
fun LoadingDialog(message: String) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(style = MaterialTheme.typography.h1, text = stringResource(id = R.string.dialog_loading_title)) },
        text = {
               Row {
                   CircularProgressIndicator(
                       color = White,
                       modifier = Modifier
                           .width(30.dp)
                           .height(30.dp),
                       strokeWidth = 2.dp
                   )
                   Spacer(
                       modifier = Modifier.width(5.dp)
                   )
                   Text(
                       text = message,
                       style = MaterialTheme.typography.h1
                   )
               }
        },
        confirmButton = {},
    )
}

@Composable
private fun DefaultErrorMessage(exception: Exception){
    Text(text = exception.message?:"Unknown error")
}