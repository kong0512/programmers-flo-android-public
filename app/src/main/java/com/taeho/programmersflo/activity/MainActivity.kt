package com.taeho.programmersflo.activity

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    private var backButtonPressed: Boolean = false

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
//            playViewModel.setLyricsData(data.lyrics)
        })

        fragmentManager = supportFragmentManager

        if(fragmentManager.fragments.isEmpty()){
            fragmentManager.beginTransaction().add(R.id.main_fragment, SongViewFragment()).disallowAddToBackStack().commit()

        }




        playViewModel.getSongData()

    }

    override fun onBackPressed() {
        if(fragmentManager.backStackEntryCount == 0){
            if(backButtonPressed){
                super.onBackPressed()
                playViewModel.releaseExoplayer()
                finish()
                System.exit(1)
            }
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            backButtonPressed = true

            CoroutineScope(Dispatchers.Main).launch {
                delay(2000L)
                backButtonPressed = false
            }
        }else{
            super.onBackPressed()
        }
    }


    fun moveToFullLyricsFragment() {
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_top, R.anim.slide_out_bottom).replace(R.id.main_fragment, FullLyricsFragment()).addToBackStack(null).commit()

    }

}