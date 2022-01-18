package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.model.res.ResBursaPengiriman
import java.util.ArrayList

class CourseAdapter(val listCourses: ArrayList<ResBursaPengiriman?>, val context: Context) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private val lastPosition = -1
    private val FADE_DURATION = 1000 //FADE_DURATION in milliseconds


    class CourseViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val kotaAsalPengiriman = viewItem.findViewById<TextView?>(R.id.txt_kota_asal_pengiriman)as TextView
        val kotaTujuanPengiriman = viewItem.findViewById<TextView?>(R.id.txt_kota_tujuan) as TextView
        val typeTruckPengiriman = viewItem.findViewById<TextView?>(R.id.txt_type_truck) as TextView
        val waktuKirimPengiriman = viewItem.findViewById<TextView?>(R.id.txt_waktu_pengiriman) as TextView
        val statusPengiriman = viewItem.findViewById<TextView?>(R.id.txt_status_pengiriman) as TextView
        val txt_kategori_barang = viewItem.findViewById<TextView?>(R.id.txt_kategori_barang) as TextView
        val relativeLayout = viewItem.findViewById<CardView?>(R.id.cv_list_pengiriman) as CardView?
    }

    override fun getItemCount(): Int = listCourses.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val viewHolder = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_pengiriman_status, parent, false)
        val itemViewHolder = CourseViewHolder(viewHolder)
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder?.kotaAsalPengiriman?.text = listCourses[position]?.kab_asal
        holder?.kotaTujuanPengiriman?.text = listCourses[position]?.kab_tujuan
        holder?.typeTruckPengiriman?.text = listCourses[position]?.jenis_kendaraan
        holder?.waktuKirimPengiriman?.text = listCourses[position]?.tgl_muat
        holder?.txt_kategori_barang?.text = listCourses[position]?.kategori_barang
        holder?.statusPengiriman?.text = listCourses[position]?.status_pengiriman
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(itemView: View, position: Int) {
        if (position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(this.myFragment.getContext(), android.R.anim.slide_in_left);
//            itemView.startAnimation(animation);
//            lastPosition = position;
            val anim = AlphaAnimation(0.0f, 1.0f)
            anim.duration = FADE_DURATION.toLong()
            itemView.startAnimation(anim)
        }
    }
}
