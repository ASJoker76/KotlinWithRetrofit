package com.example.myapplication.connection

import com.example.myapplication.model.res.ResBursaPengiriman
import com.example.myapplication.model.res.ResGetProfile
import com.example.myapplication.model.res.ResLogin
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CommentAPI {
    @POST("api_v1/login/loginAll")
    fun loginRequest(@Body reqLogin: RequestBody): Call<ResLogin>

    @POST("api_v1/Transporter/list_bursa_pengiriman")
    fun listBursaPengiriman(@Header("Authorization") authorization: String,
        @Body reqBursaPengiriman: RequestBody): Call<List<ResBursaPengiriman>>

    @POST("api_v1/Api/getProfile")
    fun getProfile(@Header("Authorization") authorization: String?): Call<ResGetProfile>
}