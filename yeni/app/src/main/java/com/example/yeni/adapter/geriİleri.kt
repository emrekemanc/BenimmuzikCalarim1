package com.example.yeni.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import com.example.yeni.R
import com.example.yeni.databinding.ActivityMainBinding
import com.example.yeni.model.User
import com.example.yeni.view.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class geriİleri (val context: Context,val binding: ActivityMainBinding){
    fun geriileri(){
        composite.add(
            userDao.getir(numara)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::geriİleri))
    }
    fun geriİleri(user: User){
        play.stop()
        val file = File(user.muzik)
        val uri= Uri.fromFile(file)
        play = MediaPlayer.create(context,uri)
        play.start()
        val byt=user.muzikGorsel
        val bitmap= BitmapFactory.decodeByteArray(byt,0,byt.size)
        binding.muzikGorsel.setImageBitmap(bitmap)
        binding.muzikAdi.text=user.muzikAdi
        a =true
        binding.baslatDurdur.setImageResource(R.drawable.durdur)
        seekBar(binding,context).seekbar()
        seekBar(binding,context).sekbar()
    }

}