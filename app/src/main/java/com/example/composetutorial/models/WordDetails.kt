package com.example.composetutorial.models

import android.util.JsonWriter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.composetutorial.util.Constants
import com.google.gson.Gson

@Entity(tableName = Constants.WordTable.TABLE_NAME)
data class WordDetails(@PrimaryKey(autoGenerate = true) val id: Long,
                       @ColumnInfo(name = Constants.WordTable.WORD) val word: String,
                       @ColumnInfo(name = Constants.WordTable.DEFINITION1) val definition1: String,
                       @ColumnInfo(name = Constants.WordTable.DEFINITION2) val definition2: String,
                       @ColumnInfo(name = Constants.WordTable.LEVEL) val diffLevel: Int,
                       @ColumnInfo(name = Constants.WordTable.SOURCE_URL) val sourceUrl: String){

}

data class ListOfDefinition(val listDefinition: List<Definition>){
    override fun toString(): String {
        return "ListOfDefinition(listDefinition=$listDefinition)"
    }
}

data class Definition(val id: Int, val definition: String){
    override fun toString(): String {
        return "Definition(id=$id, definition='$definition')"
    }
}

class DefinitionTypeConverter{

    @TypeConverter
    fun fromListOfDefinitionToString(listDefinition: ListOfDefinition):String{
        var gson = Gson()
        var jsonString = gson.toJson(listDefinition)
        return jsonString
    }

    @TypeConverter
    fun fromStringToListOfDefinition(string: String): ListOfDefinition{
        var gson = Gson()
        var listOfDefinition = gson.fromJson(string, ListOfDefinition::class.java)
        return listOfDefinition
    }
}

