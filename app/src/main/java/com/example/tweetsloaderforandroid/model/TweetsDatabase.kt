package com.example.tweetsloaderforandroid.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Tweet::class], version = 1, exportSchema = false)
abstract class TweetsDatabase : RoomDatabase() {
    abstract fun tweetsDao(): TweetsDao
}