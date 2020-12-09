package com.taeho.programmersflo.activity

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.taeho.programmersflo.R
import com.taeho.programmersflo.util.LyricsUtil
import com.taeho.programmersflo.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val playViewModel: PlayViewModel by viewModels()
    private var exoPlayer: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exoPlayer =  SimpleExoPlayer.Builder(this).build()
        songView_player.player = exoPlayer
        songView_player.showTimeoutMs = 0

        songView_player.setProgressUpdateListener { position, bufferedPosition ->
            playViewModel.setStatusForLyrics(position)
        }

        playViewModel.songLiveData.observe(this@MainActivity, Observer { data ->
            songView_title.setText(data.title)
            songView_singer.setText(data.singer)
            songView_album.setText(data.album)

            Glide.with(this)
                .load(data.image)
                .into(songView_image)

            playViewModel.setLyricsData(data.lyrics)

            exoPlayer!!.addMediaItem(MediaItem.fromUri(data.file))

            exoPlayer!!.prepare()
        })

        playViewModel.currentLyricIndex.observe(this@MainActivity, Observer { index ->
            songView_lyrics_current.setText(playViewModel.getLyrics(index))
            songView_lyrics_next.setText(playViewModel.getLyrics(index+1))

            if(index != -1){
                songView_lyrics_current.setTypeface(Typeface.DEFAULT_BOLD)
            }else{
                songView_lyrics_current.setTypeface(Typeface.DEFAULT)
            }

        })

        playViewModel.getSongData()

    }

    fun initPlayer(songURI: String){


    }
}