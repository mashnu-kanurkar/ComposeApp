package com.example.composetutorial.maingame

import com.example.composetutorial.Exceptions.InvalidInputExceptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.pow

class CharMeshProvider(private val word: String,
                       private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default) {

    //Max word length is given in util.Constants
    private lateinit var answerWord: String
    private lateinit var dispatcher: CoroutineDispatcher
    private var wordLength: Int = 0
    private lateinit var answerCharList: List<Char>
    private lateinit var decoyCharList: List<Char>
    private var decoyCharLength: Int = 0
    private var hintCharListLength: Int = 0

    init {
        answerWord = word
        dispatcher = defaultDispatcher
        wordLength = answerWord.length
    }

    suspend fun getHintCharList(): List<Char?>{
        return withContext(dispatcher){
            answerCharList = answerWord.toList()
            decoyCharList = generateDecoyChars()
            hintCharListLength = getHintCharListLength()

            val tempCharList = answerCharList + decoyCharList
            val shuffledList = tempCharList.shuffled()

            val finalCharList = mutableListOf<Char?>()
            val pyramidStructureGenerator = PyramidStructureGenerator(hintCharListLength)
            val pyramidStructure = pyramidStructureGenerator.pyramidStructure //[5, 3, 1] etc
            val maxColumnLength = pyramidStructureGenerator.maxColumnLength

            var shuffledIndex = 0
            for (columnLength in pyramidStructure){
                val nullRequired = maxColumnLength-columnLength
                if (nullRequired != 0){
                    finalCharList.addAll(List(nullRequired/2){null})
                }
                finalCharList.addAll(shuffledList.subList(shuffledIndex, columnLength+shuffledIndex))
                shuffledIndex += columnLength
                if (nullRequired != 0){
                    finalCharList.addAll(List(nullRequired/2){null})
                }
            }
            finalCharList
        }
    }

    private fun getDecoyCharListLength():Int{
        //equation of hyperbola y=a(x-h)^2+k
        //a=0.103, h=3 (min word length), k=50 (max hint margin %), y= hint margin %, x= word length
        val margin = (0.1038*((wordLength-3).toDouble().pow(2))+50).toInt() //% of hint margin
        val decoyLength = (wordLength.toDouble()*margin.toDouble())/100
        return Math.ceil(decoyLength).toInt()
    }

    private fun generateDecoyChars():List<Char>{
        decoyCharLength = getDecoyCharListLength()
        //ASCII char code from 65 to 90
        val decoyCharList = mutableListOf<Char>()
        for (index in 0 until decoyCharLength){
            val char: Char = ('A'..'Z').random()
            decoyCharList.add(char)
        }
        return decoyCharList
    }

    private fun getHintCharListLength(): Int{
        return wordLength+getDecoyCharListLength()
    }

}

class PyramidStructureGenerator(private val hintListLength : Int){
    var maxColumnLength = 5
    private set
    var maxRowLength = 5
    private set
    var pyramidStructure = mutableListOf<Int>()
    private set

    init {
        generatePyramidStructure()
    }

    private fun generatePyramidStructure(): List<Int>{
        addBasePyramidRecursively(pyramidStructure, hintListLength)
        return pyramidStructure.makeCorrectionToPyramid()
    }

    private fun MutableList<Int>.makeCorrectionToPyramid(): List<Int>{
        if (this.size > maxRowLength){
            var replaceWith = this[this.size - 1 ] + this[this.size - 2 ]
            this[this.size - 2] = replaceWith
            this.removeAt(this.size - 1)
            this.makeCorrectionToPyramid()
        }
        return this
    }

    private fun addBasePyramidRecursively(pyramidStructure: MutableList<Int>, hintListLength: Int): List<Int>{
        try {
            pyramidStructure.addAll(getBasePyramidStructure(hintListLength))
        }catch (invalidInputExceptions:InvalidInputExceptions){
            pyramidStructure.add(maxColumnLength)
            addBasePyramidRecursively(pyramidStructure, hintListLength-5)
        }
        return pyramidStructure
    }


    private fun getBasePyramidStructure(baseCount: Int): List<Int>{
        return when(baseCount){
            5 -> listOf(1, 3, 1)
            6 -> listOf(3, 3)
            8 -> listOf(5, 3)
            9 -> listOf(5, 3, 1)
            else -> throw InvalidInputExceptions()
        }
    }
}

/*
Pyramid structure
Word: 3 -> hint: 5 ->   1 + 3 + 1
Word: 4 -> hint: 6 ->   3 + 3
Word: 5 -> hint: 8 ->   5 + 3
Word: 6 -> hint: 9 ->   5 + 3 + 1
Word: 7 -> hint: 11 ->  5 + 3 + 3
Word: 8 -> hint: 13 ->  5 + 5 + 3
Word: 9 -> hint: 14 ->  5 + 5 + 3 + 1
Word: 10 -> hint: 16 -> 5 + 5 + 3 + 3
Word: 11 -> hint: 18 -> 5 + 5 + 5 + 3
Word: 12 -> hint: 19 -> 5 + 5 + 5 + 3 + 1
Word: 13 -> hint: 21 -> 5 + 5 + 5 + 3 + 3
Word: 14 -> hint: 23 -> 5 + 5 + 5 + 5 + 3
Word: 15 -> hint: 25 -> 5 + 5 + 5 + 5 + 5
 */

/*
Word: 3 -> hint: 5 ->   1 + 4
Word: 4 -> hint: 6 ->   2 + 4
Word: 5 -> hint: 8 ->   2 + 6
Word: 6 -> hint: 9 ->   1 + 8
Word: 7 -> hint: 11 ->  1 + 8 + 2
Word: 8 -> hint: 13 ->  1 + 8 + 2
Word: 9 -> hint: 14 ->  2 + 8 + 4
Word: 10 -> hint: 16 -> 2 + 8 + 6
Word: 11 -> hint: 18 -> 2 + 8 + 8
Word: 12 -> hint: 19 -> 1 + 8 + 8 + 2
Word: 13 -> hint: 21 -> 1 + 8 + 8 + 4
Word: 14 -> hint: 23 -> 1 + 8 + 8 + 6
Word: 15 -> hint: 25 -> 1 + 8 + 8 + 8
//This will be the max limit of words
Word: 16 -> hint: 27 -> 1 + 8 + 8 + 8 + 2
Word: 17 -> hint: 29 -> 1 + 8 + 8 + 8 + 4
Word: 18 -> hint: 32 -> 2 + 8 + 8 + 8 + 6
Word: 19 -> hint: 34 -> 2 + 8 + 8 + 8 + 8
Word: 20 -> hint: 36 -> 2 + 8 + 8 + 8 + 8 + 2
 */
