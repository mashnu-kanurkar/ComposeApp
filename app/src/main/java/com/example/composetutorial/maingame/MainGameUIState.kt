package com.example.composetutorial.maingame

import com.example.composetutorial.models.ListOfDefinition

data class MainGameUIState(
    val currentWord: String = "",
    val definition1: String = "",
    val definition2: String = "",
    val showDefinition2: Boolean = false,
    val level: Int = 1,
    val sourceURL: String = "",
    val hintList: List<HintChar> = listOf<HintChar>(),
    val userInputList: List<HintChar> = listOf<HintChar>(),
    val isUserAnswerWrong: Boolean = false
)