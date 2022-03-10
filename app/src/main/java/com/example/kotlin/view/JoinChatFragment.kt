package com.example.kotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlin.R
import com.example.kotlin.databinding.FragmentJoinChatBinding


class JoinChatFragment : Fragment() {

    private lateinit var binding: FragmentJoinChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJoinChatBinding.inflate(inflater, container, false)

        binding.btnJoin.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("username", binding.etUsername.text.toString())
            val fragementIntent = DashboardFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()

            transaction?.replace(R.id.fl_view, fragementIntent)
            fragementIntent.setArguments(bundle)
            transaction!!.addToBackStack(null)
            transaction?.commit()
        }

        return binding.root
    }
}