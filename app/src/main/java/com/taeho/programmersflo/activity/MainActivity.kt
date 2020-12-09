package com.taeho.programmersflo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.offline.DownloadHelper.createMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.taeho.programmersflo.R
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
            Log.d("test", position.toString())


        }

        playViewModel.songLiveData.observe(this@MainActivity, Observer { data ->
            songView_title.setText(data.title)
            songView_singer.setText(data.singer)
            songView_album.setText(data.album)

            Glide.with(this)
                .load(data.image)
                .into(songView_image)

            exoPlayer!!.addMediaItem(MediaItem.fromUri(data.file))

            exoPlayer!!.prepare()
        })

        playViewModel.getSongData()

    }

    fun initPlayer(songURI: String){


    }
}