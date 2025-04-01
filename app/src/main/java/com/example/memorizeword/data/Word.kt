package com.example.memorizeword.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    var english: String,
    var mongolian: String
)
