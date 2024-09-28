package com.example.tweetsloaderforandroid.model

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.tweetsloaderforandroid.ui.settings.SettingsFragment
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.IOException
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.jvm.Throws

class TweetsRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val tweetsDao: TweetsDao
){
    @Throws(IOException::class)
    suspend fun loadTweets() {
        val tweetsUri = loadTweetsUri() ?: return

        appContext.contentResolver.openInputStream(tweetsUri)?.use { inputStream ->
            saveTweets(formatTweets(decode(inputStream.readBytes().toString(Charsets.UTF_8))))
        }
    }

    fun getTweetsFileName(): String? {
        val tweetsUri = loadTweetsUri() ?: return null
        val cursor = appContext.contentResolver.query(tweetsUri, null, null, null, null) ?: return null
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        val fileName = cursor.getString(nameIndex)
        cursor.close()
        return fileName
    }

    suspend fun getAllTweets(): List<Tweet> {
        return tweetsDao.getAllTweets()
    }

    suspend fun searchTweets(query: String): List<Tweet> {
        return tweetsDao.findTweetsIncludingQuery(query)
    }

    private fun decode(tweetsJson: String): List<SerialTweet> {
        val jsonSerializer = Json {
            ignoreUnknownKeys = true
        }
        return jsonSerializer.decodeFromString<List<SerialTweet>>(tweetsJson.replace("window.YTD.tweets.part0 = ", ""))
    }

    private fun formatTweets(tweets: List<SerialTweet>): List<Tweet> {
        return tweets.map {
            Tweet(
                it.contents.id,
                formatDate(it.contents.createdAt),
                formatFullText(it.contents.fullText)
            )
        }
    }

    private fun formatDate(date: String): String {
        // Twitterの日付のフォーマット:
        // https://scrapbox.io/kadoyau/Twitter%E3%81%AEcreated_at%E3%82%92%E5%A4%89%E6%8F%9B%E3%81%99%E3%82%8B
        // DateTimeFormatterを使うときはlocaleを必ず指定する:
        // https://mike-neck.hatenadiary.com/entry/2017/01/01/123937
        val tweetDateTimeFormatter = DateTimeFormatter.ofPattern("EEE LLL dd HH:mm:ss ZZ yyyy", Locale.US)
        val parsedDateTime = OffsetDateTime.parse(date, tweetDateTimeFormatter)
        val jaDateTime = parsedDateTime.withOffsetSameInstant(ZoneOffset.ofHours(9))

        val jaDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")
        return jaDateTime.format(jaDateTimeFormatter)
    }

    private fun formatFullText(fullText: String): String {
        // TODO: もっと良いアプローチを探す
        // jsonSerializerでdecodeした際に特殊文字がencodeされてしまう
        // &, <, > がアウトで、', "はセーフだった
        // textの空白文字を維持しながらdecodeするのが難しいので力技
        return fullText
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
    }

    private fun loadTweetsUri(): Uri? {
        val uriStr = appContext.getSharedPreferences(SettingsFragment.TAG, Context.MODE_PRIVATE).getString(SettingsFragment.LAST_OPENED_URI_KEY, "")
        if (uriStr.isNullOrEmpty()) return null
        return Uri.parse(uriStr)
    }

    private suspend fun saveTweets(tweets: List<Tweet>) {
        tweetsDao.deleteAllTweets()
        tweetsDao.insertTweets(tweets)
    }
}