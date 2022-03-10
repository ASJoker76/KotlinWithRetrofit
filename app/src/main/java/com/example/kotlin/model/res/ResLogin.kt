package com.example.kotlin.model.res

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResLogin {
    @SerializedName("status")
    @Expose
    val status:Int? = null

    @SerializedName("username")
    @Expose
    val username:String? = null

    @SerializedName("email")
    @Expose
    val email:String? = null

    @SerializedName("token")
    @Expose
    val token:String? = null

    @SerializedName("role")
    @Expose
    val role:String? = null
}