package com.example.grzybekapk.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.grzybekapk.R

class EkranGlowny : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekran_glowny)


        if (savedInstanceState == null) {

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLay, FragEkranStartowy.newInstance(),
                    "Test")
                .replace(R.id.toolbarLayout, ToolbarClass.newInstance(),"Test2")
                .commit()
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navBar)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }




    }


}
