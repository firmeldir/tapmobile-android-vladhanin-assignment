package com.vladhanin.tapmobiletestapp

import com.vladhanin.tapmobiletestapp.data.YoutubeContractImpl
import com.vladhanin.tapmobiletestapp.data.YoutubeRetrofitService
import com.vladhanin.tapmobiletestapp.domain.DefaultMainModel
import com.vladhanin.tapmobiletestapp.domain.MainModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object DI {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.youtube.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    fun provideMainModel(): MainModel =
        DefaultMainModel(YoutubeContractImpl(retrofit.create(YoutubeRetrofitService::class.java)))
}