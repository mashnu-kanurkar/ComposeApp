package com.example.composetutorial.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetutorial.MainActivityViewModel
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.composetutorial.util.SnackbarController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(modifier: Modifier = Modifier,
               onButtonClick: ()->Unit,
               mainActivityViewModel: MainActivityViewModel
){
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()
    ComposeTutorialTheme() {
        Surface(modifier = modifier,
            color = MaterialTheme.colorScheme.background) {
            Scaffold(modifier = Modifier,
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    Log.d("Count: ", mainActivityViewModel.message)
                    if (mainActivityViewModel.message != ""){
                        SnackbarController(scope = scope).showSnackbar(snackbarHostState, mainActivityViewModel.message, "Label")
                    }

                    Column(modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Greeting(name = "")
                        Button(onClick = {
                            onButtonClick()
                        }) {
                            Text(text = "Login")
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun Greeting(name: String) {

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(text = "")
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Spelling, ")
                Text(text = name)
            }

            ElevatedButton(
                onClick = { /* TODO */ }
            ) {
                Text("Show more")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTutorialTheme {
        //MainScreen(modifier = Modifier.fillMaxWidth(), onButtonClick = {}, )
    }

}