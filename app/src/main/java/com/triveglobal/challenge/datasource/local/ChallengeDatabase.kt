package com.triveglobal.challenge.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class], version = 1)
abstract class ChallengeDatabase: RoomDatabase() {

    companion object {
        private const val BD_NAME = "challenge.db"
        fun getDatabase(context: Context): ChallengeDatabase {
            return Room.databaseBuilder(context, ChallengeDatabase::class.java, BD_NAME).build()
        }
    }

    abstract fun bookDao(): BookDao

}