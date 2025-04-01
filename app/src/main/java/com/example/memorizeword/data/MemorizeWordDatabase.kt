package com.example.memorizeword.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class MemorizeWordDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var Instance: MemorizeWordDatabase? = null

        fun getDatabase(context: Context): MemorizeWordDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MemorizeWordDatabase::class.java, "word_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}