package com.example.yeni.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.yeni.R
import com.example.yeni.adapter.RecyclerViewAdapter
import com.example.yeni.adapter.UserDao
import com.example.yeni.adapter.geriİleri
import com.example.yeni.adapter.seekBar
import com.example.yeni.database.AppDatabase
import com.example.yeni.databinding.ActivityMainBinding
import com.example.yeni.model.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File


@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding
lateinit var db:AppDatabase
lateinit var userDao:UserDao
lateinit var play:MediaPlayer
lateinit var lis:List<User>
var composite=CompositeDisposable()
var a=true
var numara:Int=1




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        play= MediaPlayer.create(this,R.raw.bip)
        binding.muzikGornum.visibility=View.GONE
        room()
        recyclerView()

    }
    fun recyclerView(){
        composite.add(userDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handeler))
    }
    private fun handeler(liste:List<User>){
        binding.RecyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=RecyclerViewAdapter(liste, binding,this )
        binding.RecyclerView.adapter=adapter
        lis= liste

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater=menuInflater
        inflater.inflate(R.menu.menum,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.menu1->{
                val intent=Intent(this,MainActivity2::class.java)

                startActivity(intent)

                true
            }else-> super.onOptionsItemSelected(item)
        }

    }
    fun baslatDurdur(view: View){
        if (a==true){
            play.pause()
            a=false
            binding.baslatDurdur.setImageResource(R.drawable.baslat)
        }else{

            play.start()
            a=true
            binding.baslatDurdur.setImageResource(R.drawable.durdur)
        }
    }
    fun ileri(view: View){
        if (numara<lis.size){
            numara++
        }
       else if (numara== lis.size){
            numara=1
        }
        geriİleri(this, binding).geriileri()
    }
    fun geri(view: View){
        if (numara>1){
            numara--
        }else if (numara==1){
            numara= lis.size
        }
        geriİleri(this, binding).geriileri()
    }


    fun room(){
         db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "User"
        ).build()
       userDao = db.userDao()
    }
}