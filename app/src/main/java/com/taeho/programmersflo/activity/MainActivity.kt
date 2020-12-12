package com.taeho.programmersflo.activity

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.taeho.programmersflo.R
import com.taeho.programmersflo.fragment.FullLyricsFragment
import com.taeho.programmersflo.fragment.SongViewFragment
import com.taeho.programmersflo.util.LyricsUtil
import com.taeho.programmersflo.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.activity_main.*

/*
Programmers 과제테스트용 Android Application
Flo-like Music Player

사용 라이브러리:
JetPack: Android Architecture Component(ViewModel, LiveData)용
Retrofit(https://square.github.io/retrofit/): HTTP 통신
Glide(https://github.com/bumptech/glide): 이미지 로딩
ExoPlayer(https://github.com/google/ExoPlayer): 음악 파일 재생용
Koin(https://github.com/InsertKoinIO/koin): 의존성 주입
 */
class MainActivity : FragmentActivity() {
    private val playViewModel: PlayViewModel by viewModels()
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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

        fragmentManager = supportFragmentManager

        if(fragmentManager.fragments.isEmpty()){
            Log.d("Fragment", "SongViewFragment Added")
            fragmentManager.beginTransaction().add(R.id.main_fragment, SongViewFragment(), "SONGVIEW_FRAGMENT").addToBackStack(null).commit()
        }



        playViewModel.getSongData()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(fragmentManager.findFragmentById(R.id.main_fragment) == null){
            playViewModel.releaseExoplayer()

            finish()
            System.exit(1)
        }
    }


    fun moveToFullLyricsFragment() {


        fragmentManager.beginTransaction().replace(R.id.main_fragment, FullLyricsFragment(), "FULLLYRICS_FRAGMENT").addToBackStack(null).commit()

    }

}