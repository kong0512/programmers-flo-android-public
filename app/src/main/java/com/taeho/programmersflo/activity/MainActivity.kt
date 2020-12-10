package com.taeho.programmersflo.activity

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.taeho.programmersflo.R
import com.taeho.programmersflo.util.LyricsUtil
import com.taeho.programmersflo.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {
    private val playViewModel: PlayViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playViewModel.createExoplayer()

        playViewModel.exoPlayer.observe(this@MainActivity, Observer { player ->
            songView_player.player = player
            songView_player.showTimeoutMs = 0
        })


        songView_player.setProgressUpdateListener { position, bufferedPosition ->
            playViewModel.setStatusForLyrics(position)
        }

        playViewModel.songLiveData.observe(this@MainActivity, Observer { data ->
            playViewModel.setFileExoplayer()
        })

        playViewModel.getSongData()

    }

}