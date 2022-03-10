package com.example.kotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.R
import com.example.kotlin.databinding.ListItemChatBinding
import com.example.kotlin.model.res.ResChat
import java.text.DateFormat
import java.util.*

class ChatAdapter(
    private val username: String)
    : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    var resultss = ArrayList<ResChat>()
    private val lastPosition = -1
    private var viewBinding: ListItemChatBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.MyViewHolder {
        viewBinding =
            ListItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(viewBinding!!)
    }

    override fun getItemCount() = resultss.size


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val resResults = resultss[position]

        val date = Date(resResults.timestamp)
        val formattedDate = DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(date)

        if(username.equals(resResults.username)){
            holder.viewBinding.clView.setBackgroundResource(R.drawable.my_balloon_shape)
            holder.viewBinding.tvNama.setText(resResults.username)
            holder.viewBinding.tvIsiChat.setText(resResults.text)
            holder.viewBinding.tvDate.setText(formattedDate)
        }
        else{
            holder.viewBinding.clView.setBackgroundResource(R.drawable.other_balloon_shape)
            holder.viewBinding.tvNama.setText(resResults.username)
            holder.viewBinding.tvIsiChat.setText(resResults.text)
            holder.viewBinding.tvDate.setText(formattedDate)

            holder.viewBinding.tvNama.setTextColor(R.color.blue)
            holder.viewBinding.tvIsiChat.setTextColor(R.color.blue)
            holder.viewBinding.tvDate.setTextColor(R.color.blue)
        }
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(itemView: View, position: Int) {
        if (position > lastPosition) {
            val anim = AlphaAnimation(0.0f, 1.0f)
            anim.duration = FADE_DURATION.toLong()
            itemView.startAnimation(anim)
        }
    }



    class MyViewHolder(binding: ListItemChatBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        val viewBinding: ListItemChatBinding

        init {
            viewBinding = binding
        }
    }

    companion object {
        private const val FADE_DURATION = 1000 //FADE_DURATION in milliseconds
    }

    fun setDataList(data :  ArrayList<ResChat>) {
        this.resultss = data
        notifyDataSetChanged()
    }
}
