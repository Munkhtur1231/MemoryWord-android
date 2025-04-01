package com.example.memorizeword.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.memorizeword.MemorizeWordApplication
import com.example.memorizeword.data.Word
import com.example.memorizeword.data.WordsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class WordUiState(
    val words: List<Word> = listOf(),
    var currentWord: Word? = null,
    val previousWord: Word? = null,
    val wordOption: String = "both",
    val isEnglishShow: Boolean = true,
    val isMongolianShow: Boolean = true
)

class MemorizeWordViewModel(
    private val wordsRepository: WordsRepository
) : ViewModel() {

    private val _wordUiState = MutableStateFlow(WordUiState())
    val wordUiState: StateFlow<WordUiState> = _wordUiState.asStateFlow()

    var userEnglishInput by mutableStateOf("")
        private set
    var userMongolianInput by mutableStateOf("")
        private set
    var currentWordIndex by mutableStateOf(0)
        private set

    init {
        loadAllWords()
    }

    fun updateUserEnglishInput(input: String) {
        userEnglishInput = input
    }

    fun updateUserMongolianInput(input: String) {
        userMongolianInput = input
    }

    fun loadAllWords() {
        viewModelScope.launch {
            val allWords = wordsRepository.getAllWordsStream().filterNotNull().first()
            _wordUiState.update { currentState ->
                currentState.copy(
                    words = allWords,
                )
            }
            _wordUiState.update { currentState ->
                if (allWords.isNotEmpty()) {
                    currentState.copy(
                        currentWord = currentState.words[currentWordIndex]
                    )
                } else {
                    currentWordIndex = 0
                    currentState.copy(
                        currentWord = null
                    )
                }
            }
        }
    }

    fun updateAllWords() {
        viewModelScope.launch {
            val allWords = wordsRepository.getAllWordsStream().filterNotNull().first()
            _wordUiState.update { currentState ->
                currentState.copy(words = allWords)
            }
        }
    }

    fun nextWord() {
        _wordUiState.update { currentState ->
            if (currentState.words.isEmpty()) {
                currentWordIndex = 0
                currentState.copy(
                    currentWord = null
                )
            } else {
                currentWordIndex = (currentWordIndex + 1) % currentState.words.size
                currentState.copy(
                    currentWord = currentState.words[currentWordIndex]
                )
            }
        }
        clearToggleVisibility()
    }

    fun previousWord() {
        _wordUiState.update { currentState ->
            if (currentState.words.isEmpty()) {
                currentWordIndex = 0
                currentState.copy(
                    currentWord = null
                )
            } else {
                currentWordIndex =
                    (currentWordIndex - 1 + currentState.words.size) % currentState.words.size
                currentState.copy(
                    currentWord = currentState.words[currentWordIndex]
                )
            }
        }
        clearToggleVisibility()
    }

    fun changeWordOption(option: String) {
        _wordUiState.update { currentState ->
            currentState.copy(
                wordOption = option,
                isEnglishShow = option != "mongolian",
                isMongolianShow = option != "english"
            )
        }
    }

    fun toggleEnglishVisibility() {
        _wordUiState.update { currentState ->
            currentState.copy(isEnglishShow = !currentState.isEnglishShow)
        }
    }

    fun toggleMongolianVisibility() {
        _wordUiState.update { currentState ->
            currentState.copy(isMongolianShow = !currentState.isMongolianShow)
        }
    }

    fun clearToggleVisibility() {
        _wordUiState.update { currentState ->
            currentState.copy(
                isEnglishShow = currentState.wordOption != "mongolian",
                isMongolianShow = currentState.wordOption != "english"
            )
        }
    }

    fun addWord() {
        if (userEnglishInput.isBlank() || userMongolianInput.isBlank()) return

        viewModelScope.launch {
            val newWord = Word(
                english = userEnglishInput,
                mongolian = userMongolianInput
            )
            wordsRepository.insertWord(newWord)

            // input ee tseverlene
            clearInput()

            // current wordoo shine ug bolgono
            _wordUiState.update { currentState ->
                currentState.copy(
                    currentWord = newWord
                )
            }
            updateAllWords()
            currentWordIndex = _wordUiState.value.words.indexOf(newWord)
        }
    }

    fun editWord() {
        if (userEnglishInput.isBlank() || userMongolianInput.isBlank()) return

        viewModelScope.launch {
            _wordUiState.update { currentState ->
                val updatedWord = currentState.currentWord?.copy(
                    english = userEnglishInput,
                    mongolian = userMongolianInput
                )

                val updatedWords = currentState.words.map { word ->
                    if (word.id == currentState.currentWord?.id) updatedWord!! else word
                }

                currentState.copy(
                    currentWord = updatedWord,
                    words = updatedWords
                )
            }
            _wordUiState.value.currentWord?.let { wordsRepository.updateWord(it) }
        }

        // input ee tseverlene
        clearInput()
    }

    fun replaceInputByCurrentWord() {
        userEnglishInput = _wordUiState.value.currentWord!!.english
        userMongolianInput = _wordUiState.value.currentWord!!.mongolian
    }

    fun clearInput() {
        userEnglishInput = ""
        userMongolianInput = ""
    }

    fun deleteWord() {
        viewModelScope.launch {
            _wordUiState.value.currentWord?.let { wordsRepository.deleteWord(it) }
            loadAllWords()
            previousWord()
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MemorizeWordApplication)
                MemorizeWordViewModel(
                    application.container.wordsRepository,
                )
            }
        }
    }
}