package com.example.composetutorial.maingame

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.composetutorial.models.WordDetails
import com.example.composetutorial.repository.WordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainGameViewModel(private val wordRepository: WordRepository): ViewModel() {

    private val TAG = "MainGameViewModel"

    private val _currentGameState = MutableStateFlow(MainGameUIState())
    val currentGameState : StateFlow<MainGameUIState> = _currentGameState.asStateFlow()

    private lateinit var _wordDetails: WordDetails

    private var _currentUserInputIndex by mutableStateOf(0)

    val validationEvent = MutableSharedFlow<ValidationEvent>()

    init {
        pickWord("PAN")
    }

    private fun pickWord(string: String){
        //get last saved game state from DB
        //write custom logic to pick word ID
        //getWord(1)
        val def1 = "The condition that distinguishes animals and plants from inorganic matter, including the capacity for growth, reproduction, functional activity, and continual change preceding death."
        val def2 = "The existence of an individual human being or animal."
        _wordDetails = WordDetails(1, string, def1, def2, 1, "https://www.google.com")
        val initialUserInputList = mutableListOf<HintChar>()
        for (num in 0 until _wordDetails.word.length){
            initialUserInputList.add(HintChar(-1, ' ', false, false))
        }
        _currentGameState.value = MainGameUIState(
            currentWord = _wordDetails.word,
            definition1= _wordDetails.definition1,
            definition2= _wordDetails.definition2,
            showDefinition2 = false,
            level = _wordDetails.diffLevel,
            sourceURL = _wordDetails.sourceUrl,
            hintList = mutableListOf<HintChar>(),
            userInputList = initialUserInputList
        )
        generateHintList(string)
        Log.d(TAG, "Current list: ${_currentGameState.value}")
    }

    fun getAdditionalHint(){
        Log.d(TAG, " Additional hint requested")
        _currentGameState.update {currentGameState ->
            currentGameState.copy(showDefinition2 = true)
        }
    }

    private fun generateHintList(word: String){
        val hintCharList = mutableListOf<HintChar>()
        var index = 0
        viewModelScope.launch {
            val lst = CharMeshProvider(word).getHintCharList()
            for (char in lst){
                if (char != null){
                    hintCharList.add(HintChar(index = index, char = char, isSelected = false, isClickable = true))
                }else{
                    hintCharList.add(HintChar(index = index, char = char, isSelected = false, isClickable = false))
                }
                index++
            }
            _currentGameState.update { currentGameState ->
                currentGameState.copy(hintList = hintCharList)
            }
        }
    }

    fun onHintCharClick(char: Char, charIndex: Int){
        Log.d(TAG, "Index : $_currentUserInputIndex")
        if (_currentUserInputIndex >=_currentGameState.value.currentWord.length){
            return
        }
        val currentInputList = _currentGameState.value.userInputList.toMutableList()
        currentInputList[_currentUserInputIndex] = currentInputList[_currentUserInputIndex].copy(index = charIndex, char = char)
        val currentHintList = _currentGameState.value.hintList.toMutableList()
        currentHintList[charIndex] = currentHintList[charIndex].copy(isSelected = true, isClickable = false)
        _currentGameState.update { currentGameState->
            currentGameState.copy(userInputList = currentInputList, hintList = currentHintList)
        }
        _currentUserInputIndex += 1
        Log.d(TAG, "Index : $_currentUserInputIndex")

    }

    fun clearUserInput(){
        Log.d(TAG, "Index : $_currentUserInputIndex")
        if (_currentUserInputIndex <= 0){
            return
        }
        val currentHintList = _currentGameState.value.hintList.toMutableList()
        val currentInputList = _currentGameState.value.userInputList.toMutableList()
        val charIndex = currentInputList[_currentUserInputIndex-1].index

        currentHintList[charIndex] = currentHintList[charIndex].copy(isSelected = false, isClickable = true)

        currentInputList[_currentUserInputIndex-1] = currentInputList[_currentUserInputIndex-1].copy(index = -1, char = ' ')

        _currentGameState.update { currentGameState->
            currentGameState.copy(userInputList = currentInputList, hintList = currentHintList)
        }
        _currentUserInputIndex -= 1
        Log.d(TAG, "Index : $_currentUserInputIndex")

    }

    fun checkUserAnswer(){
        validateInputs()
    }

    fun getWord(id: Int){
        viewModelScope.launch {
            _wordDetails = wordRepository.getWord(id).value!!
        }
    }

    private fun validateInputs(){
        val charList = _currentGameState.value.userInputList.map { it.char!! }
        val userAnswer = String(charList.toCharArray())
        Log.d(TAG, "userAnswer is $userAnswer")
        if (userAnswer.equals(_currentGameState.value.currentWord)){
            viewModelScope.launch {
                validationEvent.emit(ValidationEvent.Success)
                pickWord("MAXIMUM")
                _currentUserInputIndex = 0
            }
        }else{
            viewModelScope.launch {
                validationEvent.emit(ValidationEvent.Failed)
            }
        }
    }

}

sealed class ValidationEvent {
    object Success: ValidationEvent()
    object Failed: ValidationEvent()
}

class MainGameViewModelFactory(private val wordRepository: WordRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainGameViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MainGameViewModel(wordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}