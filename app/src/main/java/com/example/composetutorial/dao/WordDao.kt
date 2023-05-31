package com.example.composetutorial.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composetutorial.models.WordDetails
import com.example.composetutorial.util.Constants

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(word: WordDetails): Long

    @Query("SELECT * FROM ${Constants.WordTable.TABLE_NAME} WHERE ${Constants.WordTable.WORD_ID} = :id")
    fun getWord(id: Int): LiveData<WordDetails>
}