package com.taeho.programmersflo.fragment

import android.app.ActionBar
import android.content.Context
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.taeho.programmersflo.R
import com.taeho.programmersflo.activity.MainActivity
import com.taeho.programmersflo.model.Lyrics
import com.taeho.programmersflo.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.fragment_full_lyrics.*


class FullLyricsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val playViewModel: PlayViewModel by activityViewModels()
    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity

    private lateinit var closeButton: ImageButton
    private lateinit var toggleButton: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_full_lyrics, container, false)

        closeButton = view.findViewById(R.id.fulllyrics_close) as ImageButton
        toggleButton = view.findViewById(R.id.fulllyrics_toggle) as ImageButton

        playViewModel.songLiveData.observe(this@FullLyricsFragment, Observer { data ->
            lyrics_scroll.removeAllViews()

            fulllyrics_title.text = data.title
            fulllyrics_singer.text = data.singer
        })

        playViewModel.currentLyricIndex.observe(this@FullLyricsFragment, Observer { index ->
//            playViewModel.setToggle()


            val lyricsList = playViewModel.getLyricsList()


            lyrics_scroll.removeAllViews()

            for(i in lyricsList.indices) {
                val currentView = TextView(mContext)
                currentView.text = lyricsList[i].lyric
                currentView.textSize = resources.getDimension(R.dimen.fullLyrics_size)
                val param = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
                currentView.layoutParams = param

                if(index == i){
                    currentView.setTextColor(ContextCompat.getColor(mContext, R.color.currentLyrics))
                }
                else{
                    currentView.setTextColor(ContextCompat.getColor(mContext, R.color.unfocusedLyrics))
                }

                currentView.setOnClickListener {
                    if(playViewModel.isMovePositionToggled()){
                        playViewModel.setSongPosition(lyricsList[i].startTime)
                    }
                    else{
                        mActivity.supportFragmentManager.popBackStack()
                    }

                }

                lyrics_scroll.addView(currentView)
            }



        })

        playViewModel.movePositionToggled.observe(this@FullLyricsFragment, Observer {
            if(it){
                toggleButton.setColorFilter(Color.parseColor("#0000FF"), PorterDuff.Mode.SRC_IN)
            }else{
                toggleButton.colorFilter = null
            }
        })

        closeButton.setOnClickListener {
            mActivity.supportFragmentManager.popBackStack()
            Log.d("test", "Test")
        }

        toggleButton.setOnClickListener {
            playViewModel.setToggle()
        }


        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
        mActivity = requireActivity() as MainActivity

    }

}