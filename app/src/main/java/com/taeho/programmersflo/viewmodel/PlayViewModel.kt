package com.taeho.programmersflo.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.taeho.programmersflo.R
import com.taeho.programmersflo.fragment.FullLyricsFragment
import com.taeho.programmersflo.model.Lyrics
import com.taeho.programmersflo.model.SongData
import com.taeho.programmersflo.repository.SongService
import com.taeho.programmersflo.util.LyricsUtil
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayViewModel(application: Application): AndroidViewModel(application) {
    val songLiveData = MutableLiveData<SongData>()
    val exoPlayer = MutableLiveData<SimpleExoPlayer>()
    private val lyricsData = mutableListOf<Lyrics>()
    val currentLyricIndex = MutableLiveData<Int>()
    var movePositionToggled = MutableLiveData<Boolean>()
    val songService: SongService


    private val mContext = getApplication<Application>().applicationContext

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        songService = retrofit.create(SongService::class.java)

        movePositionToggled.value = false

        createExoplayer()

    }

    fun isMovePositionToggled(): Boolean {
        return movePositionToggled.value!!
    }

    fun setToggle() {
        movePositionToggled.postValue(!(movePositionToggled.value!!))

    }

    fun createExoplayer() {
        exoPlayer.postValue(SimpleExoPlayer.Builder(mContext).build())

    }

    fun setFileExoplayer() {
        exoPlayer.value!!.addMediaItem(MediaItem.fromUri(songLiveData.value!!.file))
        exoPlayer.value!!.prepare()
    }

    fun releaseExoplayer() {
        exoPlayer.value!!.release()
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

    fun getLyricsList(): List<Lyrics> {
        return lyricsData
    }

    fun setSongPosition(position: Long) {
        exoPlayer.value!!.seekTo(position)
    }
}