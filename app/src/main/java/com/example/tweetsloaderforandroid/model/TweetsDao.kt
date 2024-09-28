package com.example.tweetsloaderforandroid.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TweetsDao {
    @Query("SELECT * FROM tweets ORDER BY created_at DESC")
    suspend fun getAllTweets(): List<Tweet>

    @Query("SELECT * FROM tweets WHERE full_text LIKE '%' || :query || '%' ORDER BY created_at DESC")
    suspend fun findTweetsIncludingQuery(query: String): List<Tweet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTweets(tweets: List<Tweet>)

    @Query("DELETE FROM tweets")
    suspend fun deleteAllTweets()
}