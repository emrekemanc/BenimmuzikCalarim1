package com.example.yeni.adapter


import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yeni.R
import com.example.yeni.databinding.ActivityMainBinding
import com.example.yeni.databinding.RecyclerViewBinding
import com.example.yeni.model.User
import com.example.yeni.view.*
import java.io.File

class RecyclerViewAdapter(val itemler: List<User>, val binding: ActivityMainBinding, val context: Context, ): RecyclerView.Adapter<RecyclerViewAdapter.itemHolder>() {

    class itemHolder(val binding: RecyclerViewBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemHolder {
       val binding=RecyclerViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return itemHolder(binding)
    }

    override fun getItemCount(): Int {
        println(itemler.size)
        return itemler.size
    }

    override fun onBindViewHolder(holder: itemHolder, position: Int) {
        holder.binding.item.text=itemler.get(position).muzikAdi
        holder.itemView.setOnClickListener {
            play.stop()
            numara=position+1
            binding.muzikGornum.visibility= View.VISIBLE
            binding.muzikAdi.text=itemler.get(position).muzikAdi
            val byta=itemler.get(position).muzikGorsel
            val bitmap=BitmapFactory.decodeByteArray(byta,0,byta.size)
            binding.muzikGorsel.setImageBitmap(bitmap)
            val file=File(itemler.get(position).muzik)
            val uri=Uri.fromFile(file)
            play=MediaPlayer.create(context,uri)
            play.start()
            binding.baslatDurdur.setImageResource(R.drawable.durdur)
            a=true
            seekBar(binding,context).sekbar()
            seekBar(binding,context).seekbar()
        }
    }



}