package com.example.memorizeword.data

import kotlinx.coroutines.flow.Flow

interface WordsRepository {
    suspend fun insertWord(word: Word)
    suspend fun deleteWord(word: Word)
    suspend fun updateWord(word: Word)
    fun getAllWordsStream(): Flow<List<Word>>
    fun getWordStream(id: Int): Flow<Word?>
}