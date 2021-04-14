package com.example.fotografpaylasmafirebase.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fotografpaylasmafirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FotografPaylasmaActivity : AppCompatActivity() {
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null

    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)
        //firabase ögelerini init et
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

    }
    fun paylas(view : View){
        //depo işlemleri
        // görselimizi nereye kaydedeceğimizi refransla tek tek söyleyebiliriz
        val reference = storage.reference //reference, storage ın kendisine referans verir
        //images klasöründe secilengorsel.jpg diye bir dosya olacağını ve oraya referans veriyor

        //UUID - Universal Uniquie ID
        val uuid = UUID.randomUUID()
        val gorselIsmi = "${uuid}.jpg"

        val gorselReference = reference.child("images").child(gorselIsmi)
        //resim yüklemek uzun süreceğinden asenkron yapıp sonucunu beklemeliyiz
        if (secilenGorsel != null){
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener { taskSnapshot ->
                val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener {
                    //buradaki it:Uri! aslında nereye kaydedildiğidir
                    val downloadUrl = it.toString()
                    println(downloadUrl)
                    //Birşeyin nereye yüklendiğini ancak yüklendikten sonra referans olarak alıyoruz.
                    val guncelKullaniciEmaili = auth.currentUser!!.email.toString()
                    val yorumtext = findViewById<TextView>(R.id.yorumText)
                    val kullaniciYorumu = yorumtext.text.toString()
                    val tarih = com.google.firebase.Timestamp.now()
                    //veritabanı işlemleri
                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap.put("gorselUrl", downloadUrl)
                    postHashMap.put("kullaniciemail", guncelKullaniciEmaili)
                    postHashMap.put("kullaniciyorum", kullaniciYorumu)
                    postHashMap.put("tarih", tarih)
                    //firestoreda görünecek collectionumuzun adını verelim
                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            finish() //haberleractivity açıktı, bu işlemi yapabildiysek kapatalım
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG ).show()
                    }


                }
                println(" Fotoğraf yüklendi.")
            }.addOnFailureListener {
                Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG ).show()
            }
        }



    }
    fun gorselSec(view: View){
        //kullanıcı izni alalım
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //izin alınmamış, izin iste
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            //izin alınmışsa galeriye git
            val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verilirse yapılacaklar
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            secilenGorsel = data.data // secilen gorselin Urisi
            if(secilenGorsel != null){

                if (Build.VERSION.SDK_INT >= 28){

                    val source = ImageDecoder.createSource(this.contentResolver, secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)

                    val imageView  = findViewById<ImageView>(R.id.imageView)
                    imageView.setImageBitmap(secilenBitmap)

                }else {
                    secilenBitmap =  MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    val imageView = findViewById<ImageView>(R.id.imageView)
                    imageView.setImageBitmap(secilenBitmap)

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}