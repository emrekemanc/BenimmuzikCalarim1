package com.example.yeni.view


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.room.Room
import com.example.yeni.BuildConfig
import com.example.yeni.R
import com.example.yeni.adapter.UserDao
import com.example.yeni.database.AppDatabase
import com.example.yeni.databinding.ActivityMain2Binding
import com.example.yeni.model.User
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity2 : AppCompatActivity() {

private lateinit var db:AppDatabase
private lateinit var userDao:UserDao
private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
private lateinit var permissionLauncher: ActivityResultLauncher<String>
private lateinit var activityResultLauncher2 :ActivityResultLauncher<Intent>
private lateinit var permissionLauncher2: ActivityResultLauncher<String>
private lateinit var binding: ActivityMain2Binding
    var bitmap:Bitmap?=null
    var filePath:String?=null
    val compositeDisposable= CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        binding= ActivityMain2Binding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        yeniResim()
        yeniMuzik()
        room()


    }



    fun resimekle(view: View){
        if (Build.VERSION.SDK_INT>=VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this@MainActivity2, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity2, android.Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view, "izin lazım", Snackbar.LENGTH_LONG).setAction("ver") {
                        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)

                    }.show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)

                }

            } else {
                val intentToGalary = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalary)
            }
        }else{
            if (ContextCompat.checkSelfPermission(this@MainActivity2, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity2, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "izin lazım", Snackbar.LENGTH_LONG).setAction("ver") {
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                    }.show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                }

            } else {
                val intentToGalary = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalary)
            }

        }




    }
    fun yeniResim(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode== RESULT_OK){
                val intentFromResult=result.data
                if (intentFromResult!=null){
                    val imageData=intentFromResult.data
                    if (imageData!=null){
                        try {

                            if (Build.VERSION.SDK_INT>=28){
                                val source=ImageDecoder.createSource(this@MainActivity2.contentResolver,imageData)
                                bitmap=ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(bitmap)

                            }else{
                                bitmap=MediaStore.Images.Media.getBitmap(contentResolver,imageData)
                                binding.imageView.setImageBitmap(bitmap)
                            }
                        }catch (_:Exception){

                        }}

                }
            }

        }
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if (result){
                val intentToGalary=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalary)

            }
            else{
                Toast.makeText(this@MainActivity2,"izin vermen lazım yoksa olmaz",Toast.LENGTH_LONG).show()

            }
        }
    }

    fun muzikEkle(view: View){
        if (Build.VERSION.SDK_INT>=VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this@MainActivity2, android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity2, android.Manifest.permission.READ_MEDIA_AUDIO)) {
                    Snackbar.make(view, "izin lazım", Snackbar.LENGTH_LONG).setAction("ver") {
                        permissionLauncher2.launch(android.Manifest.permission.READ_MEDIA_AUDIO)

                    }.show()
                } else {
                    permissionLauncher2.launch(android.Manifest.permission.READ_MEDIA_AUDIO)

                }

            } else {
                val intentToGalary = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher2.launch(intentToGalary)
            }
        }else {
            if (ContextCompat.checkSelfPermission(this@MainActivity2, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity2, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ) {
                    Snackbar.make(view, "izin lazım", Snackbar.LENGTH_LONG).setAction("ver") {
                        permissionLauncher2.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                    }.show()
                } else {
                    permissionLauncher2.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                }

            } else {
                val intentToGalary =
                    Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher2.launch(intentToGalary)
            }
        }
        }
    fun yeniMuzik(){
        activityResultLauncher2=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode== RESULT_OK){
                val intentFromResult=result.data
                if (intentFromResult!=null){
                    val imageData=intentFromResult.data
                    if (imageData!=null){
                        filePath= getRealPathFromURI(imageData)
                        binding.textView.text=filePath

                    }
                }

            }

        }

        permissionLauncher2=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if (result){
                val intentToAudio=Intent(Intent.ACTION_PICK,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher2.launch(intentToAudio)

            }
            else{
                Toast.makeText(this@MainActivity2,"izin vermen lazım yoksa olmaz",Toast.LENGTH_LONG).show()
            println(result)
            }
        }
    }
    fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }


    fun kaydet(view:View){

        val kucukresim=resimküçüt(bitmap!!,400)

        val outputStream= ByteArrayOutputStream()
        kucukresim.compress(Bitmap.CompressFormat.PNG,50,outputStream)
        val byteArray=outputStream.toByteArray()
        val user=User(binding.sarkiAdi.text.toString(),byteArray,filePath.toString())


        compositeDisposable.add(
            userDao.insertAll(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlerResponse)
        )
    }
    fun handlerResponse(){
        val ıntent=Intent(this,MainActivity::class.java)
        ıntent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(ıntent)
    }

    fun resimküçüt(ımage:Bitmap,maxsize:Int):Bitmap{
        var width=ımage.width
        var height=ımage.height
        val bitmaratio:Double=width.toDouble()/height.toDouble()

        if (bitmaratio>1){
            height=maxsize
            val yeniWidth=height*bitmaratio
            width=yeniWidth.toInt()

        }else if (bitmaratio<1){
            width=maxsize
            val yeniHeight=width*bitmaratio
            height=yeniHeight.toInt()

        }else{
            width=maxsize
            height=maxsize
        }
return Bitmap.createScaledBitmap(ımage,width,height,true)
    }
    fun room(){
       db= Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "User"
        ).build()
         userDao = db.userDao()
    }
    override fun onDestroy() {
        super.onDestroy()
    compositeDisposable.clear()
    }






}