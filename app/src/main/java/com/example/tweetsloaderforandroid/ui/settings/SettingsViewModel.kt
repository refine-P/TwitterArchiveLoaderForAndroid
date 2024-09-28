package com.example.tweetsloaderforandroid.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetsloaderforandroid.model.TweetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val tweetsRepository: TweetsRepository
) : ViewModel() {

    private val _archiveName = MutableLiveData<String>()
    val archiveName: LiveData<String> = _archiveName

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun loadTweets() = viewModelScope.launch(dispatcher) {
        val fileName = getTweetsFileName()
        _archiveName.postValue(if (fileName.isNullOrEmpty()) {
            "Warning: no archive is being loaded."
        } else {
            "Loading: $fileName"
        })

        tweetsRepository.loadTweets()
        setLoadedFileName(fileName)
    }

    fun setLoadedFileName(fileName: String?) {
        _archiveName.postValue(if (fileName.isNullOrEmpty()) {
            "Loaded archive name: no archive is loaded."
        } else {
            "Loaded archive name: $fileName"
        })
    }

    fun getTweetsFileName(): String? {
        return tweetsRepository.getTweetsFileName()
    }
}