package com.example.composetutorial.util

object Constants {
    const val seperator = ""
    object MainDatabase{
        const val NAME = "Main_Database"
    }

    object WordTable{
        const val TABLE_NAME = "word_table"
        const val WORD_ID = "id"
        const val WORD = "word"
        const val DEFINITION1 = "definition1"
        const val DEFINITION2 = "definition2"
        const val LEVEL = "level"
        const val SOURCE_URL = "source_url"
    }

    object WordAndCharLimit{
        const val MIN_WORD_LENGHT = 3
        const val MAX_WORD_LENGHT = 5
        const val MAX_HINT_MARGIN = 50 // extra 40% words will be added along with answer chars in the hint list
        const val MIN_HINT_MARGIN = 20
        const val ROW_kEY = "Row"
        const val COLUMN_KEY = "Column"

    }
}