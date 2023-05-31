package com.example.composetutorial.maingame

sealed class MainGameUIEvent {
    data class UserCharEntered(val char: Char): MainGameUIEvent()
    data class UserCharCleared(val char: Char): MainGameUIEvent()
    object Submit: MainGameUIEvent()
}