package com.taeho.programmersflo.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.taeho.programmersflo.R
import com.taeho.programmersflo.databinding.ActivityMainBinding
import com.taeho.programmersflo.fragment.FullLyricsFragment
import com.taeho.programmersflo.fragment.SongViewFragment
import com.taeho.programmersflo.viewmodel.PlayViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

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

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mainBinding.root)


        playViewModel.exoPlayer.observe(this@MainActivity, Observer { player ->
            mainBinding.songViewPlayer.player = player
            mainBinding.songViewPlayer.showTimeoutMs = 0
        })


        mainBinding.songViewPlayer.setProgressUpdateListener { position, _ ->
            playViewModel.setStatusForLyrics(position)
        }


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
                exitProcess(1)
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