package com.syed.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.syed.bookhub.*
import com.syed.bookhub.fragment.AboutAppFragment
import com.syed.bookhub.fragment.DashboardFragment
import com.syed.bookhub.fragment.FavouritesFragment
import com.syed.bookhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
             var previousMenuItem: MenuItem? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)
        setUpToolbar()
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, DashboardFragment()).addToBackStack("DashBoard").commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard_fragment)

        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when(it.itemId){
                R.id.dashboard ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        DashboardFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Dashboard"
                    navigationView.setCheckedItem(R.id.dashboard_fragment)

                }
                R.id.favourites ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        FavouritesFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Favourites"
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        ProfileFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Profile"
                }
                R.id.about_app ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        AboutAppFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="About App"
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }
    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title="TOOLBAR TITLE"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        val id =item.itemId
        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        if(frag !is DashboardFragment){
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, DashboardFragment()).commit()
            supportActionBar?.title="Dashboard"
        }
        else{
            super.onBackPressed()
        }

    }
}