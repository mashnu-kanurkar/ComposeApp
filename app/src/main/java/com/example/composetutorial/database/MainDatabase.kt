package com.example.composetutorial.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.composetutorial.dao.WordDao
import com.example.composetutorial.models.DefinitionTypeConverter
import com.example.composetutorial.models.WordDetails
import com.example.composetutorial.util.Constants


@Database(entities = arrayOf(WordDetails::class), version = 1, exportSchema = false)
@TypeConverters(DefinitionTypeConverter::class)
public abstract class MainDatabase: RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object{
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MainDatabase? = null
        fun getDatabase(context: Context): MainDatabase{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    Constants.MainDatabase.NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
}