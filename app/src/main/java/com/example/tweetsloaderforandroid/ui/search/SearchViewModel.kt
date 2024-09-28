package com.example.tweetsloaderforandroid.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetsloaderforandroid.model.Tweet
import com.example.tweetsloaderforandroid.model.TweetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val tweetsRepository: TweetsRepository
) : ViewModel() {

    private val _tweets = MutableLiveData<List<Tweet>>()
    val tweets: LiveData<List<Tweet>> = _tweets

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun searchTweets(query: String) = viewModelScope.launch(dispatcher) {
        val searchedTweets = tweetsRepository.searchTweets(query)
        _tweets.postValue(searchedTweets)
    }
}