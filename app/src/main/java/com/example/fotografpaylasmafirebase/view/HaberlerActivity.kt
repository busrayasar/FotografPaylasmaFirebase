package com.example.fotografpaylasmafirebase.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fotografpaylasmafirebase.R
import com.example.fotografpaylasmafirebase.adapter.HaberRecyclerAdapter
import com.example.fotografpaylasmafirebase.databinding.ActivityHaberlerBinding
import com.example.fotografpaylasmafirebase.databinding.ActivityMainBinding
import com.example.fotografpaylasmafirebase.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
//import kotlinx.android.synthetic.main.activity_haberler.*


class HaberlerActivity : AppCompatActivity() {


    private lateinit var  auth: FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var recyclerViewAdapter : HaberRecyclerAdapter
    var postList = ArrayList<Post>()
    private lateinit var binding: ActivityHaberlerBinding //viewbinding için ekledim

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)

        binding = ActivityHaberlerBinding.inflate(layoutInflater) //view binding için bu root viewı kullanıcaz
        val view = binding.root
        setContentView(view)

        auth =  FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance() //db yi oluşturduk
        verileriAl()
        //haberlerAdapterda aldığımız görünümleri buraya koyalım
        //alt alta recycler_rowların oluşturulacağını söylemek için LayoutManager kullanırız
        //var layoutMngr = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //recyclerView.layoutManager=layoutMngr
        recyclerViewAdapter = HaberRecyclerAdapter(postList)
        binding.recyclerView.adapter =recyclerViewAdapter
       // recyclerView.adapter =recyclerViewAdapter


    }
    fun verileriAl(){
        //real time güncellemelerle db den veri çekelim
        database.collection("Post").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener { snapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null){
                Toast.makeText(this, firebaseFirestoreException.localizedMessage, Toast.LENGTH_LONG).show()
            }else{ //hata yoksa çekilen verimizi işleyelim
                if(snapshot != null){
                    if (!snapshot.isEmpty ){
                        //snapshotumda kesin veri var şu an kontrollerden sonra
                        val documents = snapshot.documents

                        postList.clear() //önceden kalan bişey varsa temizle
                        for (document in documents){
                            //firebaseden verileri alalım
                            val kullaniciEmail = document.get("kullaniciemail") as String
                            println("Firestore'dan çektiğimiz kullanici email: "+ kullaniciEmail)
                            val kullaniciYorumu = document.get("kullaniciyorum") as String
                            val gorselUrl = document.get("gorselUrl") as String

                            val indirilenPost = Post(kullaniciEmail, kullaniciYorumu, gorselUrl)
                            postList.add(indirilenPost)

                        }
                        //yeni veri geldi kendini yenile
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }

            }
        }
    }
///Githuba bağlamaya çalışıyorum

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.secenekler_menusu, menu)
        return super.onCreateOptionsMenu(menu)


    }
    //hangi itemın seçildiğini burada kontrol ediyoruz
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.foto_paylas){
            //fotoğraf paylaşma aktivitesine gidilecek
            val intent = Intent(this, FotografPaylasmaActivity::class.java)
            startActivity(intent)
            //burada intenti finish etmiyoruz çünkü kullanıcı fotoğraf paylaşmaktan vazgeçebilir


        }else if(item.itemId == R.id.cikis_yap){
            auth.signOut()
            val intent = Intent(this, KullaniciActivity::class.java )
            startActivity(intent)
            finish()

        }

        return super.onOptionsItemSelected(item)
    }

}