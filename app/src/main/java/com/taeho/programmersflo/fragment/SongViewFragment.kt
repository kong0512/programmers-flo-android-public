package com.taeho.programmersflo.fragment

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.taeho.programmersflo.R
import com.taeho.programmersflo.activity.MainActivity
import com.taeho.programmersflo.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.fragment_song_view.*


class SongViewFragment : Fragment() {
    private val playViewModel: PlayViewModel by activityViewModels()
    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_song_view, container, false)

        playViewModel.songLiveData.observe(viewLifecycleOwner, Observer { data ->
            songView_title.text = data.title
            songView_singer.text = data.singer
            songView_album.text= data.album

            Glide.with(this)
                .load(data.image)
                .into(songView_image)

        })

        playViewModel.currentLyricIndex.observe(viewLifecycleOwner, Observer { index ->


            songView_lyrics_current.text = playViewModel.getLyrics(index)
            songView_lyrics_next.text =  playViewModel.getLyrics(index+1)



            if(index != -1){
                songView_lyrics_current.setTextColor(ContextCompat.getColor(mContext, R.color.currentLyrics))
            }else{
                songView_lyrics_current.setTextColor(ContextCompat.getColor(mContext, R.color.unfocusedLyrics))
            }


            songView_lyrics_layout.setOnClickListener {
                mActivity.moveToFullLyricsFragment()
            }
        })




        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
        mActivity = requireActivity() as MainActivity

    }

}