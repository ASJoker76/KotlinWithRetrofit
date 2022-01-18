package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener
import com.example.myapplication.connection.CommentAPI
import com.example.myapplication.connection.Retro

import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.model.res.ResLogin
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    //ViwBinding. activity_main ==> ActivityMainBinding
    private var binding: ActivityLoginBinding? = null
    var email = ""
    var password = ""
    var tipe = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        loadsession()
//hardcode
        binding!!.etEmail.setText("khairikiya@gmail.com")
        binding!!.etPassword.setText("password")

        binding!!.btnLoginLog.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                email = binding!!.etEmail.text.toString()
                password = binding!!.etPassword.text.toString()
                tipe = 1
                getCommentApi()
            }
        })
    }

    private fun loadsession() {
        val prefs = baseContext.getSharedPreferences("login", MODE_PRIVATE)
        val token = prefs.getString("token", "")
        if (token != "") {
            pindahhalaman()
        }
    }

    private fun pindahhalaman() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    fun getCommentApi(){
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)
        jsonObject.put("tipe", tipe)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val reqLogin = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val retro = Retro().getRetroClientInstance().create(CommentAPI::class.java)
        retro.loginRequest(reqLogin).enqueue(object : Callback<ResLogin>{
            override fun onFailure(call: Call<ResLogin>, t: Throwable) {
//                TODO("Not yet implemented")
                Log.e("Failed", t.message.toString())
            }

            override fun onResponse(call: Call<ResLogin>, response: Response<ResLogin>) {
                //TODO("Not yet implemented")
                val comment = response.body()
                val code = response.code()
                val resLogin = response.body()

                Log.e("Hasil", comment.toString())
                Log.e("code", code.toString())

                if(code==200){
                    SweetAlertDialog(this@LoginActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Login Berhasil")
                        .setConfirmClickListener { sDialog ->
                            sDialog.dismissWithAnimation()

                            //init share pref
                            val editor = getSharedPreferences("login", MODE_PRIVATE).edit()
                            editor.putString("token", resLogin?.token)
                            editor.apply()

                            pindahhalaman()

                        }
                        .show()
                }
                else if(code==400){
                    SweetAlertDialog(this@LoginActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error Login")
                        .setConfirmClickListener { sDialog ->
                            sDialog.dismissWithAnimation()

                        }
                        .show()
                }
                else{
                    SweetAlertDialog(this@LoginActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error Login")
                        .setConfirmClickListener { sDialog ->
                            sDialog.dismissWithAnimation()

                        }
                        .show()
                }
            }
        })
    }
}