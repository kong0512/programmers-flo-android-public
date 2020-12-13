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
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayViewModel(application: Application): AndroidViewModel(application) {
    val songLiveData = MutableLiveData<SongData>()
    val exoPlayer = MutableLiveData<SimpleExoPlayer>()
    val lyricsData = MutableLiveData<MutableList<Lyrics>>()
    val currentLyricIndex = MutableLiveData<Int>()
    var movePositionToggled = MutableLiveData<Boolean>()
    private val songService: SongService by inject(SongService::class.java)

    private val mContext = getApplication<Application>().applicationContext

    init {


        movePositionToggled.value = false
        lyricsData.postValue(mutableListOf())
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
            var songData = songService.fetchSongData("song.json")

            try{
                songLiveData.postValue(songData)
                setLyricsData(songData.lyrics)
            }catch(t: Throwable) {
                Log.e("Error", t.toString())
            }
        }




    }

    fun setLyricsData(rawLyrics: String) {
        lyricsData.postValue(LyricsUtil.ParseLyrics(rawLyrics))
        currentLyricIndex.postValue(-1)
    }

    fun setStatusForLyrics(currentTime:Long) {
        currentLyricIndex.postValue(LyricsUtil.checkCurrentViewableLyrics(lyricsData.value!!, currentTime))
    }

    fun getLyrics(index: Int): String{
        if(lyricsData == null || lyricsData.value!!.isEmpty() || index>=lyricsData.value!!.size){
            return ""
        }

        if(index == -1){
            return lyricsData.value!![0].lyric
        }
        else{
            return lyricsData.value!![index].lyric
        }
    }

    fun getLyricsList(): List<Lyrics> {
        return lyricsData.value!!
    }

    fun setSongPosition(position: Long) {
        exoPlayer.value!!.seekTo(position)
    }
}