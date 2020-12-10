package com.taeho.programmersflo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.taeho.programmersflo.R
import com.taeho.programmersflo.viewmodel.PlayViewModel


class FullLyricsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val playViewModel: PlayViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_full_lyrics, container, false)



        return view
    }


}