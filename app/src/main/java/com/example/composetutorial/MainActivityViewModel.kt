package com.example.composetutorial

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    private val _message = getMessage()
    val message: String
    get() = _message

}

private fun getMessage() = "Message"