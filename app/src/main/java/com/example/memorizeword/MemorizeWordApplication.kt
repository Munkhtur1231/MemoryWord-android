package com.example.memorizeword

import android.app.Application
import com.example.memorizeword.data.AppContainer
import com.example.memorizeword.data.AppDataContainer

class MemorizeWordApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}