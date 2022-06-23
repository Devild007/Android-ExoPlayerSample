package com.example.sampleexoplayer.ui.view.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampleexoplayer.device.utility.DataState
import com.example.sampleexoplayer.device.utility.SessionManager
import com.example.sampleexoplayer.domain.PlayWith
import com.example.sampleexoplayer.domain.VideoLists

class ViewModel() : ViewModel() {

    private val _playWith = MutableLiveData<DataState<PlayWith>>()
    val playWith: LiveData<DataState<PlayWith>>
        get() = _playWith

    var playWithDetails: PlayWith = PlayWith()
    var videoLists: VideoLists = VideoLists()

    fun playLoading() {
        _playWith.value = DataState.Loading
    }

    fun playDetails(playWith: PlayWith, videoLists: VideoLists) {
        playWithDetails = playWith
        this.videoLists = videoLists
        _playWith.value = DataState.Success(playWith)
        /*Log.e("playDetails", "playWithDetails: ${Gson().toJson(playWithDetails)}")
        Log.e("playDetails", "videoLists: ${Gson().toJson(this.videoLists)}")*/
    }

    fun getPlayWith(): PlayWith {
        return playWithDetails
    }

}