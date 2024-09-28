package com.example.tweetsloaderforandroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerialTweet(
    @SerialName("tweet") val contents: SerialTweetContents
)

@Serializable
data class SerialTweetContents(
    @SerialName("id") val id: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("full_text") val fullText: String
)

@Entity(tableName = "tweets")
data class Tweet(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "full_text") val fullText: String
)
