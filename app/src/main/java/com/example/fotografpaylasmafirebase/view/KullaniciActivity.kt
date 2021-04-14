package com.example.fotografpaylasmafirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.fotografpaylasmafirebase.R
import com.google.firebase.auth.FirebaseAuth

class KullaniciActivity : AppCompatActivity() {

    private lateinit var  auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth= FirebaseAuth.getInstance()
        //giriş yapmış olan kullanıcının uygulamayı tekrar açtığında tekrar giriş yapmaması için
        val guncelKullanici = auth.currentUser
        if(guncelKullanici != null){
            val intent = Intent(this, HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun girisYap(view : View){
        val emailText = findViewById<TextView>(R.id.emailText)
        val emailT = emailText.text.toString()

        val passwordText = findViewById<TextView>(R.id.passwordText)
        val passwordT = passwordText.text.toString()

        auth.signInWithEmailAndPassword(emailT, passwordT).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                val guncelKullanici = auth.currentUser?.email.toString()
                Toast.makeText(this, "Hoşgeldin ${guncelKullanici}", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()

        }


    }
    fun kayitOl(view : View){

        val emailText = findViewById<TextView>(R.id.emailText)
        val emailT = emailText.text.toString()

        val passwordText = findViewById<TextView>(R.id.passwordText)
        val passwordT = passwordText.text.toString()

        auth.createUserWithEmailAndPassword(emailT, passwordT).addOnCompleteListener {task ->
            //asenkron
            if (task.isSuccessful){
                //diğer aktiviteye git
                val intent = Intent(applicationContext, HaberlerActivity::class.java)
                startActivity(intent)
                finish()

            }

        }.addOnFailureListener{ exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }
}