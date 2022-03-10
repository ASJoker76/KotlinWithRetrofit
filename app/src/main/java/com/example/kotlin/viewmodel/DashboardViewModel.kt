package com.example.kotlin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.model.res.ResChat
import pokemon.co.id.connection.RetroInstance
import pokemon.co.id.connection.RetroService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {

    //var chatLiveData: MutableLiveData<List<ResChat>>
    val chatLiveData = MutableLiveData<List<ResChat>>()
    val dataerror = MutableLiveData<String>()

//    init {
//        chatLiveData = MutableLiveData<List<ResChat>>()
//    }

    fun getRecyclerListDataObserver(): MutableLiveData<List<ResChat>>? {
        return chatLiveData
    }

    fun panggilapi() {
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.getMessage()
        call.enqueue(object : Callback<List<ResChat>> {
            override fun onFailure(call: Call<List<ResChat>>, t: Throwable) {
                dataerror?.postValue(t.message)
            }

            override fun onResponse(call: Call<List<ResChat>>, response: Response<List<ResChat>>) {
                if(response.isSuccessful){
                    Log.e("isi data", response.body().toString())
                    chatLiveData?.postValue(response.body())
                } else {
                    dataerror?.postValue(response.code().toString())
                }
            }
        })
    }
}