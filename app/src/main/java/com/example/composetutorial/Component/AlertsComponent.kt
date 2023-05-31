package com.example.composetutorial.Component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InfoSnackBar(snackbarHostState: SnackbarHostState,
                 modifier: Modifier = Modifier,
                 onDismiss: ()->Unit) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = {snackbarData ->
            Snackbar(modifier = Modifier.padding(4.dp),
                action = {
                snackbarData.visuals.actionLabel?.let {actionLabel ->
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = snackbarData.visuals.actionLabel?:"Dismiss")
                    }
                }
            }) {
                Text(text = snackbarData.visuals.message)
            }
    }, modifier = Modifier)


}


@Composable
fun ErrorSnackBar(message: String, ) {
    Snackbar(containerColor = Color.Red) {
        Text(text = message)
    }
    
}

@Preview
@Composable
fun SnackBarPreview() {
    ErrorSnackBar(message = "Oops! something went wrong")
}