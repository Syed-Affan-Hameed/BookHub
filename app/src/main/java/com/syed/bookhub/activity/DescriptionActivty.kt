package com.syed.bookhub.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.syed.bookhub.R
import com.syed.bookhub.database.BookDatabase
import com.syed.bookhub.database.BookEntity
import com.syed.bookhub.util.ConnectionManager
import okhttp3.Headers
import org.json.JSONObject


class DescriptionActivty : AppCompatActivity() {

    lateinit var txtBookName :TextView
    lateinit var txtBookAuthor : TextView
    lateinit var txtBookPrice : TextView
    lateinit var txtBookRating :TextView
    lateinit var imgBookImage : ImageView
    lateinit var txtBookDesc :TextView
    lateinit var btnAddToFav : Button
    lateinit var progressBar : ProgressBar
    lateinit var progressLayout: RelativeLayout
    var bookId: String?="100"
    lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description_activty)

        txtBookName=findViewById(R.id.txtBookName)
        txtBookAuthor=findViewById(R.id.txtBookAuthor)
        txtBookPrice=findViewById(R.id.txtBookPrice)
        txtBookRating=findViewById(R.id.txtBookRating)
        imgBookImage=findViewById(R.id.imgBookImage)
        txtBookDesc=findViewById(R.id.txtBookDesc)
        btnAddToFav=findViewById(R.id.btnAddtoFav)
        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility= View.VISIBLE
        progressLayout =findViewById(R.id.ProgressLayout)
        progressLayout.visibility=View.VISIBLE
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"


        if(intent!=null)
        {
            bookId= intent.getStringExtra("book_Id")
        }
        else{
            finish()
            Toast.makeText(this@DescriptionActivty,"Some error",Toast.LENGTH_SHORT).show()
        }
        if(bookId=="100")
        {
            finish()
            Toast.makeText(this@DescriptionActivty,"Some error",Toast.LENGTH_SHORT).show()
        }
        val queue= Volley.newRequestQueue(this@DescriptionActivty)
        val url="http://13.325.250.119/v1/book/get_book/"
        val jsonParams=JSONObject()
                jsonParams.put("book_id",bookId)
        if (ConnectionManager().checkConnectivty(this@DescriptionActivty)) {


            val jsonRequest = object : JsonObjectRequest(
                Request.Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            val bookImageUrl=bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)
                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuthor.text = bookJsonObject.getString("author")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookRating.text = bookJsonObject.getString("rating")
                            txtBookDesc.text = bookJsonObject.getString("description")

                            val bookEntity=BookEntity (
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookRating.text.toString(),
                                txtBookDesc.text.toString(),
                                    bookImageUrl
                            )
                            val checkFav= DBAsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav=checkFav.get()
                            if(isFav){
                                btnAddToFav.text="Remove from Favourites"
                                val favColor= ContextCompat.getColor(applicationContext, R.color.colorFavourite)
                                btnAddToFav.setBackgroundColor(favColor)
                            }
                            else{
                                btnAddToFav.text="Add to Favourites"
                                val noFavColor=ContextCompat.getColor(applicationContext,R.color.design_default_color_primary)
                                btnAddToFav.setBackgroundColor(noFavColor)
                            }
                            btnAddToFav.setOnClickListener {
                                if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get())
                            {
                               val async=DBAsyncTask(applicationContext,bookEntity,2).execute()
                               val result=async.get()
                               if(result){
                                   Toast.makeText(this@DescriptionActivty,"Book Added to favourites",Toast.LENGTH_SHORT).show()
                                   btnAddToFav.text="Remove From Favourites"
                                   val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                                   btnAddToFav.setBackgroundColor(favColor)

                               }
                                else{
                                   Toast.makeText(this@DescriptionActivty, "Some  error", Toast.LENGTH_SHORT)
                                           .show()
                               }
                            }
                                else{
                                    val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result=async.get()
                                    if(result){
                                        Toast.makeText(this@DescriptionActivty, "Book removed From favourites", Toast.LENGTH_SHORT)
                                                .show()
                                        btnAddToFav.text="Add to Favourites"
                                        val nofavColor=ContextCompat.getColor(applicationContext,R.color.design_default_color_primary)
                                        btnAddToFav.setBackgroundColor(nofavColor)

                                    }
                                    else{
                                        Toast.makeText(this@DescriptionActivty, "Some  error", Toast.LENGTH_SHORT)
                                                .show()
                                    }

                                }
                            }
                         } else {
                            Toast.makeText(this@DescriptionActivty, "ERROR!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@DescriptionActivty, "Some error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@DescriptionActivty, "Some Volley error", Toast.LENGTH_SHORT)
                        .show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["tokens"] = "7efda86e9569a1"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
        else{
            val dialog = AlertDialog.Builder(this@DescriptionActivty)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivty)
            }
            dialog.create()
            dialog.show()

        }

    }
    class DBAsyncTask( val context: Context, val bookEntity: BookEntity,val Mode: Int) : AsyncTask<Void, Void, Boolean>(){
        val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when(Mode){
                1->{
                        val book:BookEntity?= db.BookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book!=null
                }
                2->{
                        db.BookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{
                    db.BookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
}