package com.example.composetutorial.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.composetutorial.dao.WordDao
import com.example.composetutorial.database.MainDatabase
import com.example.composetutorial.models.WordDetails

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class WordRepository(private val mainDatabase: MainDatabase) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(wordDetails: WordDetails){
        mainDatabase.wordDao().insert(wordDetails)

    }

    fun getWord(id: Int): LiveData<WordDetails>{
        return mainDatabase.wordDao().getWord(id)
    }



}