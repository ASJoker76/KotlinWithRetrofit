package com.example.kotlin.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.kotlin.R
import com.example.kotlin.adapter.ChatAdapter
import com.example.kotlin.connection.PieSocketListener
import com.example.kotlin.databinding.FragmentDashboardBinding
import com.example.kotlin.model.res.ResChat
import com.example.kotlin.viewmodel.DashboardViewModel
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import pokemon.co.id.connection.Host.BASE_URL_CHAT

class DashboardFragment : Fragment() {

    private lateinit var username: String
    private lateinit var chatArrayList: java.util.ArrayList<ResChat>
    private lateinit var chatAdapter: ChatAdapter
    private var viewModel: DashboardViewModel? = null
    private lateinit var binding: FragmentDashboardBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        loadsession()
        loadtable()
        loadclick()
        //callApiList();
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        viewModel?.chatLiveData?.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "onCreate: $it")
            chatArrayList.addAll(it)
            chatAdapter?.setDataList(it as ArrayList<ResChat>)
        })
        viewModel?.dataerror?.observe(viewLifecycleOwner, Observer {

        })
        viewModel?.panggilapi()

        return binding.root
    }

    private fun loadclick() {
        binding.btnKirim.setOnClickListener {

            if(binding.etIsiPesan.text.isNotEmpty()){
                val client: OkHttpClient = OkHttpClient();

                Log.d("PieSocket", "Connecting");
                var apiKey =
                    "oCdCMcMPQpbvNjUIzqtvF1d2X2okWpDQj4AwARJuAgtjhzKxVEjQU6IdCjwm"; //Demo key, get yours at https://piesocket.com
                var channelId = 1;

                val request: Request = Request
                    .Builder()
                    .url(BASE_URL_CHAT +"?username=$username")
                    .build()
                val listener = PieSocketListener()
                val ws: WebSocket = client.newWebSocket(request, listener)

                ws.send(binding.etIsiPesan.text.toString())
                binding.etIsiPesan.text.clear()
//                chatArrayList.clear()
//                viewModel?.panggilapi()
//                chatAdapter?.setDataList(chatArrayList)
                val bundle = Bundle()
                bundle.putString("username", username)
                val fragementIntent = DashboardFragment()
                val transaction = activity?.supportFragmentManager?.beginTransaction()

                transaction?.replace(R.id.fl_view, fragementIntent)
                fragementIntent.setArguments(bundle)
                transaction!!.addToBackStack(null)
                transaction?.commit()
            }
            else{
                SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Chat Kosong")
                    .setConfirmClickListener { sDialog ->
                        sDialog.dismissWithAnimation()
                    }
                    .show()
            }

        }

    }

    private fun loadsession() {
        val bundle = this.arguments
        if (bundle != null) {
            username = bundle?.getString("username").toString()
        }
    }

    private fun loadtable() {
        chatArrayList = ArrayList<ResChat>()
        chatAdapter = ChatAdapter(username)
        binding.rvChat.setAdapter(AlphaInAnimationAdapter(chatAdapter!!))
        binding.rvChat.setLayoutManager(
            GridLayoutManager(
                getActivity(),
                1,
                GridLayoutManager.VERTICAL,
                false
            )
        )
        val alphaInAnimationAdapter = AlphaInAnimationAdapter(chatAdapter!!)
        alphaInAnimationAdapter.setDuration(1000)
        alphaInAnimationAdapter.setInterpolator(OvershootInterpolator())
        alphaInAnimationAdapter.setFirstOnly(false)
    }

}