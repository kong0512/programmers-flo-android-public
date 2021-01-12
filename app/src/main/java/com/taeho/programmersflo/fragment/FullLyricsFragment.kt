package com.taeho.programmersflo.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.taeho.programmersflo.R
import com.taeho.programmersflo.activity.MainActivity
import com.taeho.programmersflo.databinding.FragmentFullLyricsBinding
import com.taeho.programmersflo.viewmodel.PlayViewModel



class FullLyricsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val playViewModel: PlayViewModel by activityViewModels()
    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity

    private var fullLyricsBinding: FragmentFullLyricsBinding? = null

    private lateinit var closeButton: ImageButton
    private lateinit var toggleButton: ImageButton

    private val binding get() = fullLyricsBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fullLyricsBinding  = FragmentFullLyricsBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeButton = view.findViewById(R.id.fulllyrics_close) as ImageButton
        toggleButton = view.findViewById(R.id.fulllyrics_toggle) as ImageButton

        playViewModel.songLiveData.observe(viewLifecycleOwner, Observer { data ->
            binding.lyricsScroll.removeAllViews()

            binding.fulllyricsTitle.text = data.title
            binding.fulllyricsSinger.text = data.singer
        })

        playViewModel.lyricsData.observe(viewLifecycleOwner, Observer { lyricsList ->
            binding.lyricsScroll.removeAllViews()

            for(i in lyricsList.indices) {
                val currentView = View.inflate(mActivity, R.layout.lyrics_elements, null) as TextView
                currentView.text = lyricsList[i].lyric

                currentView.setOnClickListener {
                    if(playViewModel.isMovePositionToggled()){
                        playViewModel.setSongPosition(lyricsList[i].startTime)
                    }
                    else{
                        mActivity.supportFragmentManager.popBackStack()
                    }

                }

                binding.lyricsScroll.addView(currentView)
            }
        })

        playViewModel.currentLyricIndex.observe(viewLifecycleOwner, Observer { index ->

            for(i in 0 until  binding.lyricsScroll.childCount){
                val tempText =  binding.lyricsScroll[i] as TextView
                if(i == index){
                    tempText.setTextColor(ContextCompat.getColor(mContext, R.color.currentLyrics))
                }
                else {
                    if(tempText.currentTextColor == ContextCompat.getColor(mContext, R.color.currentLyrics)) {
                        tempText.setTextColor(ContextCompat.getColor(mContext, R.color.unfocusedLyrics))
                    }
                }

            }


        })

        playViewModel.movePositionToggled.observe(viewLifecycleOwner, Observer {
            if(it){
                toggleButton.setColorFilter(Color.parseColor("#0000FF"), PorterDuff.Mode.SRC_IN)
            }else{
                toggleButton.colorFilter = null
            }
        })

        closeButton.setOnClickListener {
            mActivity.supportFragmentManager.popBackStack()
        }

        toggleButton.setOnClickListener {
            playViewModel.setToggle()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
        mActivity = requireActivity() as MainActivity

    }

}