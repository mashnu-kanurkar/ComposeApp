package com.example.composetutorial.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composetutorial.MainActivityViewModel
import com.example.composetutorial.ui.screens.MainActivityScreen
import com.example.composetutorial.ui.theme.ComposeTutorialTheme

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    var _count = MutableLiveData<Int>(0)
    var count: LiveData<Int> = _count
    var _message = MutableLiveData<String>("message $count")
    var message: LiveData<String> = _message
    val mainActivityViewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeTutorialTheme {
                MainActivityScreen(modifier = Modifier.fillMaxWidth(),
                    onButtonClick = {goToLoginActivity()},
                mainActivityViewModel = mainActivityViewModel)
            }

        }
    }

    fun goToLoginActivity(){
        Log.d(TAG, "Clicked the button")
        startActivity(Intent(this, MainGameActivity::class.java))
    }
}