package com.taeho.programmersflo.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.taeho.programmersflo.R
import com.taeho.programmersflo.model.Lyrics
import com.taeho.programmersflo.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.fragment_full_lyrics.*


class FullLyricsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val playViewModel: PlayViewModel by activityViewModels()
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_full_lyrics, container, false)

        playViewModel.songLiveData.observe(this@FullLyricsFragment, Observer { data ->
            lyrics_scroll.removeAllViews()
        })

        playViewModel.currentLyricIndex.observe(this@FullLyricsFragment, Observer { index ->



            val lyricsList = playViewModel.getLyricsList()


            lyrics_scroll.removeAllViews()

            for(i in lyricsList.indices) {
                val currentView = TextView(mContext)
                currentView.text = lyricsList[i].lyric

                if(index == i){
                    currentView.setTextColor(ContextCompat.getColor(mContext, R.color.currentLyrics))
                }
                else{
                    currentView.setTextColor(ContextCompat.getColor(mContext, R.color.unfocusedLyrics))
                }

                currentView.setOnClickListener {
                    playViewModel.setSongPosition(lyricsList[i].startTime)
                }

                lyrics_scroll.addView(currentView)
            }



        })


        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

}