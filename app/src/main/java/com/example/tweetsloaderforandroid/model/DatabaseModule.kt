package com.example.tweetsloaderforandroid.model

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideTweetsDao(@ApplicationContext appContext: Context): TweetsDao {
        return Room.databaseBuilder(
            appContext, TweetsDatabase::class.java, "tweets-cache"
        ).build().tweetsDao()
    }
}