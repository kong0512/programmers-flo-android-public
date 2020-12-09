package com.taeho.programmersflo.repository

import com.taeho.programmersflo.model.SongData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SongService {
    @GET("{jsonName}")
    suspend fun fetchSongData(@Path("jsonName") jsonName: String): SongData
}