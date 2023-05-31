package com.example.composetutorial

import android.app.Application
import com.example.composetutorial.database.MainDatabase
import com.example.composetutorial.repository.WordRepository

class MainApplication: Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val mainDatabase by lazy { MainDatabase.getDatabase(this) }
    val wordRepository by lazy { WordRepository(mainDatabase) }
}