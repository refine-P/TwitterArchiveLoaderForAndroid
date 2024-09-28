package com.example.tweetsloaderforandroid.ui.home

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
class HomeViewModel @Inject constructor(
    private val tweetsRepository: TweetsRepository
) : ViewModel() {
    private val _tweets = MutableLiveData<List<Tweet>>()
    val tweets: LiveData<List<Tweet>> = _tweets

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun initTweets() = viewModelScope.launch(dispatcher) {
        val allTweets = tweetsRepository.getAllTweets()
        _tweets.postValue(allTweets)
    }
}