package com.taeho.programmersflo.repository

import com.taeho.programmersflo.model.SongData
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SongService {
    @GET("{jsonName}")
    fun fetchSongData(@Path("jsonName") jsonName: String): Call<SongData>
}