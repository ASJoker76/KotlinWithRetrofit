package com.example.myapplication.connection

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro {
    fun getRetroClientInstance(): Retrofit{
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("https://adacademy.id/big-dport/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}