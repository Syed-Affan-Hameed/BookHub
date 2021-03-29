package com.syed.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.syed.bookhub.R
import com.syed.bookhub.activity.DescriptionActivty
import com.syed.bookhub.model.Book

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtBookId: TextView = view.findViewById(R.id.txtBookId)
        val txtBookName: TextView = view.findViewById(R.id.txtBookName)
        val txtBookAuthor: TextView= view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice: TextView=view.findViewById(R.id.txtBookPrice)
        val txtBookRating: TextView=view.findViewById(R.id.txtBookRating)
        val imgBookImage: ImageView =view.findViewById(R.id.imgBookImage)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)

        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book=itemList[position]
        holder.txtBookId.text=book.bookId
        holder.txtBookName.text=book.bookName
        holder.txtBookAuthor.text=book.bookAuthor
        holder.txtBookPrice.text=book.BookPrice
        holder.txtBookRating.text=book.bookRating
       // holder.imgBookImage.setImageResource(book.Bookimage)
        Picasso.get().load(book.BookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage);
        holder.llContent.setOnClickListener{
            Toast.makeText(context,"These are Clickable!! ${holder.txtBookName.text}",Toast.LENGTH_SHORT).show()
            val intent= Intent(context,DescriptionActivty::class.java)
            intent.putExtra("book_Id",book.bookId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return itemList.size
    }
}