package com.example.composetutorial.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.composetutorial.MainApplication
import com.example.composetutorial.maingame.*
import com.example.composetutorial.repository.WordRepository
import com.example.composetutorial.ui.screens.MainGameScreen

class MainGameActivity : ComponentActivity() {

    private lateinit var wordRepository: WordRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wordRepository = WordRepository((application as MainApplication).mainDatabase)
        val mainGameViewModel: MainGameViewModel = ViewModelProvider(this, MainGameViewModelFactory(wordRepository)).get(
            MainGameViewModel::class.java)
        setContent {
            MainGameScreen(mainGameViewModel = mainGameViewModel)
        }
    }
}