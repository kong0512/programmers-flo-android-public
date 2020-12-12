package com.taeho.programmersflo.di

import com.taeho.programmersflo.repository.SongService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var retrofitModule = module {
    single<SongService> {
        Retrofit.Builder()
            .baseUrl("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SongService::class.java)
    }
}

var diModule = listOf(retrofitModule)