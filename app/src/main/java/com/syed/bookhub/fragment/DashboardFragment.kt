package com.syed.bookhub.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.media.AudioAttributesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.syed.bookhub.R
import com.syed.bookhub.adapter.DashboardRecyclerAdapter
import com.syed.bookhub.model.Book
import com.syed.bookhub.util.ConnectionManager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import java.util.Collections.sort
import kotlin.Comparator
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val i=0
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnCheckInternet: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
  val books= arrayListOf("Brief History of Time","1001 Inventions","The Da Vinci Code","Sherlock Holmes","The Perks of being a wallflower","Ready Player Two","The Fault in Our Stars","Anne Frank","The Lost City of Atlantis","Hamlet","1984: George Orwell","The Lost Symbol","The Ministry of Utmost Happiness","Inferno","Digital Fortress","Rich Dad Poor Dad")
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
 val bookinfolist= arrayListOf<Book>()
    var ratingComparator= Comparator<Book>{book1,book2->
        if(book1.bookRating.compareTo(book2.bookRating,true)==0){
            book1.bookName.compareTo(book2.bookRating,true)
        }
    else{
            book1.bookRating.compareTo(book2.bookRating,true)
    }
    }/*(
Book("Brief History of time","Stephen Hawking","5","£8.99",R.drawable.the_brief_history_of_time),
Book("1001 Inventions","Salim T.S. Al-Hassani","4.8","₹3000",R.drawable.inventions_1001),
Book("The Da Vinci Code","Dan Brown","4.7","₹5000",R.drawable.da_vinci_code),
        Book("Sherlock Holmes","Sir Arthur Conan Doyle","5","₹399",R.drawable.sherlock_holmes),
        Book("The Perks of being a WallFLower","Stephen Chobsky","4.2","₹218",R.drawable.the_perks_of_being_a_wallflower),
        Book("Ready Player Twor","Ernest Cline","4.2","₹218",R.drawable.ready_player_two),
        Book("The Fault in Our Stars","John Green","4.2","₹218",R.drawable.the_fault_in_our_stars),
        Book("Anne Frank","Anne Frank","4.2","₹218",R.drawable.anne_frank),
        Book("The Lost City of Atlantis","Plato","4.2","₹218",R.drawable.the_lost_sity_of_atlantis),
        Book("Hamlet","William Shakespeare","4.2","₹218",R.drawable.hamlet),
        Book("1984: George Orwell","George Orwell","4.2","₹218",R.drawable.geogre_orwell_1984),
        Book("The Lost Symbol","Dan Brown","4.2","₹218",R.drawable.the_lost_symbol),
        Book("The Ministry of Utmost Happiness","Arundhati Roy","4.2","₹218",R.drawable.the_ministry_of_utmost_happiness),
        Book("Inferno","Dan Brown","4.2","₹218",R.drawable.inferno),
        Book("Digital Fortress","Dan Brown","4.2","₹218",R.drawable.digital_fortress),
        Book("Rich Dad Poor Dad","Robert Kiyoski","4.2","₹218",R.drawable.rich_dad_poor_dad),


        )*/

/*  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
        param1 = it.getString(ARG_PARAM1)
        param2 = it.getString(ARG_PARAM2)
    }
}*/

override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment

    val view =inflater.inflate(R.layout.fragment_dashboard, container, false)
    setHasOptionsMenu(true)
    recyclerView=view.findViewById(R.id.recyclerDashboard)
    btnCheckInternet=view.findViewById(R.id.btnCheckInternet)
    progressLayout=view.findViewById(R.id.progressLayout)
    progressBar=view.findViewById(R.id.progressBar)
    progressLayout.visibility=View.VISIBLE
    btnCheckInternet.setOnClickListener {
        if(ConnectionManager().checkConnectivty(activity as Context)){
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Success")
            dialog.setMessage("Internet Connection Found")
            dialog.setPositiveButton("Ok") { text, listener ->
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
            }
            dialog.create()
            dialog.show()

        }
        else{
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Ok") { text, listener ->
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
            }
            dialog.create()
            dialog.show()

        }
    }


    layoutManager=LinearLayoutManager(activity)


    val queue = Volley.newRequestQueue(activity as Context)
    val url="http://13.235.250.119/v1/book/fetch_books/"
    try {
        progressLayout.visibility=View.GONE
        if (ConnectionManager().checkConnectivty(activity as Context)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                println("The Responsive $it")
                val success = it.getBoolean("success")
                if (success) {
                    val data = it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val BookJsonObject = data.getJSONObject(i)
                        val BookObject = Book(BookJsonObject.getString("book_id"),
                                BookJsonObject.getString("name"),
                                BookJsonObject.getString("author"),
                                BookJsonObject.getString("rating"),
                                BookJsonObject.getString("price"),
                                BookJsonObject.getString("image"))
                        bookinfolist.add(BookObject)
                        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookinfolist)
                        recyclerView.adapter = recyclerAdapter
                        recyclerView.layoutManager = layoutManager

                        recyclerView.addItemDecoration(
                                DividerItemDecoration(
                                        recyclerView.context, (layoutManager as LinearLayoutManager).orientation
                                )
                        )

                    }
                } else {
                    Toast.makeText(activity as Context, "ERROR!", Toast.LENGTH_SHORT).show()
                }
            },
                    Response.ErrorListener {
                       if(activity!=null) {
                           Toast.makeText(
                               activity as Context,
                               "Volley Error Occurred ",
                               Toast.LENGTH_SHORT
                           ).show()

                       }
                    }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "7efda86e9569a1"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
    }
    catch(e: JSONException){
        Toast.makeText(activity as Context,"Some Error $e has occurred",Toast.LENGTH_SHORT).show()
    }
    return view
}
fun OnCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?){
    inflater?.inflate(R.menu.menu_dashboard,menu)
}
   fun OnOptionsItemSelected(item: MenuItem,inflater: MenuInflater?):Boolean{
        val id=item?.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookinfolist,ratingComparator)
            bookinfolist.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
/*  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
}*/
}