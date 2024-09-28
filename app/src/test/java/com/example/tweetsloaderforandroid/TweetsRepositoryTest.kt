package com.example.tweetsloaderforandroid

import android.content.ContentResolver
import android.content.Context
import androidx.core.content.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tweetsloaderforandroid.model.Tweet
import com.example.tweetsloaderforandroid.model.TweetsDao
import com.example.tweetsloaderforandroid.model.TweetsRepository
import com.example.tweetsloaderforandroid.ui.settings.SettingsFragment
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*

@RunWith(AndroidJUnit4::class)
class TweetsRepositoryTest {
    private val appContext = ApplicationProvider.getApplicationContext<Context>()
    private val contentResolver = mock<ContentResolver>()
    private val tweetsDao = mock<TweetsDao>()
    private val tweetsRepository = TweetsRepository(appContext, contentResolver, tweetsDao)
    private val dispatcher = StandardTestDispatcher()

    @Test
    fun loadTweets() = runTest(dispatcher) {
        val dummyInputStream = """window.YTD.tweets.part0 = [
            {
                "tweet" : {
                    "id" : "1794445060504985832",
                    "created_at" : "Sat May 25 19:06:56 +0000 2024",
                    "full_text" : "テスト用ダミーデータ"
                }
            }
        ]
        """.byteInputStream()
        appContext.getSharedPreferences(SettingsFragment.TAG, Context.MODE_PRIVATE).edit {
            putString(SettingsFragment.LAST_OPENED_URI_KEY, "dummyUri")
        }
        whenever(contentResolver.openInputStream(any())).thenReturn(dummyInputStream)

        tweetsRepository.loadTweets()

        val expectedTweets = listOf(Tweet(
            "1794445060504985832",
            "2024年05月26日 04:06",
            "テスト用ダミーデータ"
        ))
        verify(tweetsDao, times(1)).insertTweets(expectedTweets)
    }
}