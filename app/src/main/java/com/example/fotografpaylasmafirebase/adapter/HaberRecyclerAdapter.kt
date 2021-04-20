package com.example.fotografpaylasmafirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fotografpaylasmafirebase.R
import com.example.fotografpaylasmafirebase.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*


//ViewHolderlar bir köprü görevi görür görünümü tutarak
class HaberRecyclerAdapter(val postlist : ArrayList<Post>): RecyclerView.Adapter<HaberRecyclerAdapter.PostHolder>() {
    class PostHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
    //implement members
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        //inflater ile oluşturduğumuz recycler_row u buraya bağlıycaz
        val inflater = LayoutInflater.from(parent.context)
        //inflaterla görünümü oluşturalım
        val view = inflater.inflate(R.layout.recycler_row, parent)
        return PostHolder(view) //PostHolder döndürüyoruz inflaterla oluşturduğumuz viewı vererek
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        //holder parametresini kullnarak ben PostHolder sınıfından oluşturulan objeye ulaşabiliyorum
        holder.itemView.recycler_row_user_email.text = postlist[position].kullaniciemail
        holder.itemView.recycler_row_user_yorumu.text = postlist[position].kullaniciyorum
        Picasso.get().load(postlist[position].gorselUrl).into(holder.itemView.recycler_row_imageview)
        //picasso ile db den resmi çekip imageviewa  yükleriz

    }

    override fun getItemCount(): Int {
        return postlist.size
    }


}