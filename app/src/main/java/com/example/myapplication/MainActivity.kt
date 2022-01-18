package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener
import com.bumptech.glide.Glide
import com.example.myapplication.adapter.CourseAdapter
import com.example.myapplication.connection.CommentAPI
import com.example.myapplication.connection.Retro

import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.res.ResBursaPengiriman
import com.example.myapplication.model.res.ResGetProfile
import com.google.gson.Gson
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var token: String? = ""
    private var screenPengirimanAdapter: CourseAdapter? = null
    private val listCourse = arrayListOf<ResBursaPengiriman?>()

    private var binding: ActivityMainBinding? = null

    private val page = 0
    private val LIMIT = 10
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        getSession()
        getdata()
        getProfile()
        getApi()
        getButton()
    }

    private fun getButton() {
        binding!!.lblLogout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                SweetAlertDialog(this@MainActivity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Apakah Anda Yakin?")
                    .setContentText("ingin keluar dari Aplikasi ini")
                    .setCancelText("Tidak")
                    .setConfirmText("Lanjut")
                    .showCancelButton(true)
                    .setCancelClickListener { sDialog -> sDialog.cancel() }
                    .setConfirmClickListener { sweetAlertDialog ->
                        sweetAlertDialog.dismiss()
                        val preferences: SharedPreferences =
                            this@MainActivity.getSharedPreferences("login", 0)
                        preferences.edit().clear().apply()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        this@MainActivity.finish()
                        startActivity(intent)
                    }
                    .show()
            }
        })
    }

    private fun getSession() {
        val prefs: SharedPreferences = getBaseContext().getSharedPreferences("login", MODE_PRIVATE)
        token = prefs.getString("token", "")
        //val token2 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ImQzYzI3NDUzM2Q5YTJmNGRkODIxYzYzODY1Y2FhMjdmNWU3MWZiMTE1NzAwNWVhODBkYmYyYWE5OWFlNzYzYzIwZTI4YjE0Njc4MTU4MjczYzc3OTI5MWZlZGQ5YTMwZjZhMjUxMDYxMTdjMzQ3NzRmNDVhNDM3MDQwNWY4M2ZmdytGMVdscnh4Zms5MTZqdXc5djUweElDZEE9PSIsInVzZXJuYW1lIjoiUFQgRGFub2UgU2VkamFodGVyYSBBYmFkaWUiLCJ0b2tlbiI6IlNLSmZkUXNsbDdLVEhNZzIiLCJ0aW1lc3RhbXAiOjE2NDAwNjExMDh9.wyTJ-w5LKasePbqMFH5Lmtbhf29Jwz1scSfhzDlUomA"

        Log.e("token", token.toString())
    }

    private fun getProfile() {
        val retro2 = Retro().getRetroClientInstance().create(CommentAPI::class.java)
        val call = retro2.getProfile(token)
        if (token != null) {
            call.enqueue(object : retrofit2.Callback<ResGetProfile> {
                override fun onFailure(call: Call<ResGetProfile>?, t: Throwable?) {
                    Log.e("retrofit", "call failed")
                }

                override fun onResponse(call: Call<ResGetProfile>?, response: Response<ResGetProfile>?) {
                    var gson = Gson()
                    val comment = response?.body()
                    val code = response?.code()
                    val resGetProfile = response!!.body()

                    Log.e("Hasil", gson.toJson(comment))
                    Log.e("code", code.toString())

                    Glide.with(this@MainActivity)
                        .load(resGetProfile?.profile_pic_path)
                        .centerCrop()
                        .into(binding!!.civProfile)
                }

            })
        }
    }

    private fun getApi() {
        val jsonObject = JSONObject()
        jsonObject.put("status", "18")
        jsonObject.put("limit", LIMIT)
        jsonObject.put("offset", page * LIMIT)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val reqBursaPengiriman = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        val retro = Retro().getRetroClientInstance().create(CommentAPI::class.java)
        if (token != null) {
            retro.listBursaPengiriman(token!!,reqBursaPengiriman).enqueue(object : retrofit2.Callback<List<ResBursaPengiriman>> {
                override fun onResponse(call: Call<List<ResBursaPengiriman>>, response: Response<List<ResBursaPengiriman>>)
                {
                    val resTenderTransporterList = response.body()
                    var gson = Gson()
                    val comment = response.body()
                    val code = response.code()

                    Log.e("Hasil", gson.toJson(comment))
                    Log.e("code", code.toString())

                    if (response.isSuccessful) {

                        if (resTenderTransporterList != null) {
                            listCourse.addAll(resTenderTransporterList)
                        }
                        screenPengirimanAdapter?.notifyDataSetChanged()

                    } else {

                    }
                }

                override fun onFailure(call: Call<List<ResBursaPengiriman>>, t: Throwable) {
                    Log.e("Failed", t.message.toString())
                }
            })
        }
    }

    private fun getdata() {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.rvPengirimanScreen?.layoutManager = mLayoutManager
        binding?.rvPengirimanScreen?.setHasFixedSize(true)
        screenPengirimanAdapter = CourseAdapter(listCourse, applicationContext)
        binding?.rvPengirimanScreen?.adapter = AlphaInAnimationAdapter(screenPengirimanAdapter!!)
        binding?.rvPengirimanScreen?.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
        binding!!.rvPengirimanScreen.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == listCourse.size - 1) {
                        //bottom of list!
                        getApi()
                        isLoading = true
                    }
                }
            }
        })
    }
}
