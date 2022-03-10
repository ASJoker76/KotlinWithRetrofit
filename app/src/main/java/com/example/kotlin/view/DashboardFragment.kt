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
import com.example.kotlin.adapter.ChatAdapter
import com.example.kotlin.connection.MessageListener
import com.example.kotlin.connection.WebSocketManager
import com.example.kotlin.databinding.FragmentDashboardBinding
import com.example.kotlin.model.res.ResChat
import com.example.kotlin.viewmodel.DashboardViewModel
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import io.ktor.http.cio.websocket.*
import pokemon.co.id.connection.Host.BASE_URL_CHAT

class DashboardFragment : Fragment(), MessageListener {

    private lateinit var username: String
    private lateinit var chatArrayList: java.util.ArrayList<ResChat>
    private lateinit var chatAdapter: ChatAdapter
    private var viewModel: DashboardViewModel? = null
    private lateinit var binding: FragmentDashboardBinding
    private var socket: WebSocketSession? = null


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
        viewModel?.panggilapi()
        viewModel?.getRecyclerListDataObserver()?.observe(viewLifecycleOwner, Observer<List<ResChat>>{
            if(it != null) {
                //update the adapter
                chatArrayList.addAll(it)
                chatAdapter?.setDataList(it as ArrayList<ResChat>)
            } else {
                Log.e("isi data", "")
            }
        })

        return binding.root
    }

    private fun loadclick() {
        kotlin.run {
            WebSocketManager.connect()
        }
        binding.btnKirim.setOnClickListener {
            if (WebSocketManager.sendMessage(binding.etIsiPesan.text.toString())){
                //addText(username)
                binding.etIsiPesan.text.clear()
                viewModel?.panggilapi()
                chatAdapter?.setDataList(chatArrayList as ArrayList<ResChat>)
            }
        }

    }

    private fun loadsession() {
        val bundle = this.arguments
        if (bundle != null) {
            username = bundle?.getString("username").toString()
            WebSocketManager.init(BASE_URL_CHAT+"?username=$username", this)
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


    override fun onConnectSuccess() {
        addText("Connect\n")
    }

    override fun onConnectFailed() {
        addText("Not Connect\n")
    }

    override fun onClose() {
        addText("Connect Close\n")
    }

    override fun onMessage(text: String?) {
        addText("Pesan：$text\n")
    }

    private fun addText(text: String?) {
        Log.e("status", text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.close()
    }
}