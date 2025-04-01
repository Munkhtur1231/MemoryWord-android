package com.example.memorizeword.data

import android.content.Context

interface AppContainer {
    val wordsRepository: WordsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val wordsRepository: WordsRepository by lazy {
        OfflineWordsRepository(MemorizeWordDatabase.getDatabase(context).wordDao())
    }
}