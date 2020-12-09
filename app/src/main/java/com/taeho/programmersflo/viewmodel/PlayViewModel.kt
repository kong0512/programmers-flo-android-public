package com.taeho.programmersflo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taeho.programmersflo.R
import com.taeho.programmersflo.model.Lyrics
import com.taeho.programmersflo.model.SongData
import com.taeho.programmersflo.repository.SongService
import com.taeho.programmersflo.util.LyricsUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayViewModel: ViewModel() {
    val songLiveData = MutableLiveData<SongData>()
    private val lyricsData = mutableListOf<Lyrics>()
    val currentLyricIndex = MutableLiveData<Int>()
    val songService: SongService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        songService = retrofit.create(SongService::class.java)


    }

    fun getSongData(){
        viewModelScope.launch(Dispatchers.IO) {
            songLiveData.postValue(songService.fetchSongData("song.json"))
        }
    }

    fun setLyricsData(rawLyrics: String) {
        lyricsData.clear()
        lyricsData.addAll(LyricsUtil.ParseLyrics(rawLyrics))
        currentLyricIndex.postValue(-1)
    }

    fun setStatusForLyrics(currentTime:Long) {
        currentLyricIndex.postValue(LyricsUtil.checkCurrentViewableLyrics(lyricsData, currentTime))
    }

    fun getLyrics(index: Int): String{
        if(lyricsData.isEmpty() || index>=lyricsData.size){
            return ""
        }

        if(index == -1){
            return lyricsData[0].lyric
        }
        else{
            return lyricsData[index].lyric
        }
    }
}