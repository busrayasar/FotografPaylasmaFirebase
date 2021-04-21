package com.example.fotografpaylasmafirebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.RecyclerView
import com.example.fotografpaylasmafirebase.databinding.ActivityHaberlerBinding
import com.example.fotografpaylasmafirebase.databinding.ActivityMainBinding
import com.example.fotografpaylasmafirebase.databinding.RecyclerRowBinding
import com.example.fotografpaylasmafirebase.model.Post
import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.recycler_row.view.*


//ViewHolderlar bir köprü görevi görür görünümü tutarak
class HaberRecyclerAdapter(val postlist : ArrayList<Post>): RecyclerView.Adapter<HaberRecyclerAdapter.PostHolder>() {

    //holderda itemwiew değilde binding olarak tanımla yapılıyor
    class PostHolder(val binding :  RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        //Holder classımızda binding yapacağımız itemView ın tipini XML den türetilen binding tipinde yapmalıyız
        // Bizim recycler row umuzun XML'li activity_haberler'di bu yüzden itemView:  ActivityHaberlerBinding olarak tanımladık

    }
    //implement members
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    /*
        //inflater ile oluşturduğumuz recycler_row u buraya bağlıycaz
        val inflater = LayoutInflater.from(parent.context)
        //inflaterla görünümü oluşturalım
        val view = inflater.inflate(R.layout.recycler_row, parent)
        return PostHolder(view) //PostHolder döndürüyoruz inflaterla oluşturduğumuz viewı vererek
     */
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val mypost : Post = postlist[position]
        holder.binding.recyclerLLayout


        //holder parametresini kullnarak ben PostHolder sınıfından oluşturulan objeye ulaşabiliyorum
        holder.binding.recyclerRowUserEmail.text = postlist[position].kullaniciemail
        holder.binding.recyclerRowUserYorumu.text= postlist[position].kullaniciyorum
        Picasso.get().load(postlist[position].gorselUrl).into(holder.binding.recyclerRowImageview)
        //picasso ile db den resmi çekip imageviewa  yükleriz

    }

    override fun getItemCount(): Int {
        return postlist.size
    }


}