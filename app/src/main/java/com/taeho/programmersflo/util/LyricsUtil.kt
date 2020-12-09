package com.taeho.programmersflo.util

import android.util.Log
import com.taeho.programmersflo.model.Lyrics

class LyricsUtil {
    companion object {
        fun ParseLyrics(lyrics: String): MutableList<Lyrics> {
            val lyricList = mutableListOf<Lyrics>()
            val tempList = lyrics.split("\n").toList()

            try {
                for (i in 0 until (tempList.size)) {
                    val tempLyricString = tempList[i].split("[", "]").filter{ it.isNotEmpty() }

                    lyricList.add(Lyrics(tempLyricString[1], ParseTime(tempLyricString[0])))

                }
            }catch (error :Exception){
                Log.e("Error from Parsing:", error.toString())
            }




            return lyricList
        }

        private fun ParseTime(timeStr:String): Long {
            var time: Long = 0L


            try {
                val temp = timeStr.split(":")

                time = (temp[2].toLong()) + (temp[1].toLong() * 1000L) + (temp[0].toLong() * 1000*60)
            }catch (error :Exception){
                Log.e("Error from parse time:", error.toString())
                Log.e("Time String:", timeStr)
            }

            return time
        }

        fun checkCurrentViewableLyrics(lyrics: List<Lyrics>, time: Long): Int {
            var result = -1
            for(i in lyrics.indices){
                if(i == lyrics.size-1 && lyrics[i].startTime <= time){
                    result = i
                    break
                }
                else if(lyrics[i].startTime <= time && lyrics[i+1].startTime > time){
                    result = i
                    break
                }
            }

            return result
        }
    }
}