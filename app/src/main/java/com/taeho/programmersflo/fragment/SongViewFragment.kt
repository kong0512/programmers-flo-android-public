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
import com.taeho.programmersflo.databinding.FragmentFullLyricsBinding
import com.taeho.programmersflo.databinding.FragmentSongViewBinding
import com.taeho.programmersflo.viewmodel.PlayViewModel
import org.koin.android.ext.android.bind


class SongViewFragment : Fragment() {
    private val playViewModel: PlayViewModel by activityViewModels()
    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity

    private var songViewBinding: FragmentSongViewBinding? = null

    private val binding get() = songViewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        songViewBinding  = FragmentSongViewBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playViewModel.songLiveData.observe(viewLifecycleOwner, Observer { data ->
            binding.songViewTitle.text = data.title
            binding.songViewSinger.text = data.singer
            binding.songViewAlbum.text= data.album

            Glide.with(this)
                .load(data.image)
                .into(binding.songViewImage)

        })

        playViewModel.currentLyricIndex.observe(viewLifecycleOwner, Observer { index ->


            binding.songViewLyricsCurrent.text = playViewModel.getLyrics(index)
            binding.songViewLyricsNext.text =  playViewModel.getLyrics(index+1)



            if(index != -1){
                binding.songViewLyricsCurrent.setTextColor(ContextCompat.getColor(mContext, R.color.currentLyrics))
            }else{
                binding.songViewLyricsCurrent.setTextColor(ContextCompat.getColor(mContext, R.color.unfocusedLyrics))
            }


            binding.songViewLyricsLayout.setOnClickListener {
                mActivity.moveToFullLyricsFragment()
            }
        })


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
        mActivity = requireActivity() as MainActivity

    }

}