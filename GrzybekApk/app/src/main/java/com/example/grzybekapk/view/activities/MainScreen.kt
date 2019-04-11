package com.example.grzybekapk.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.example.grzybekapk.R
import com.example.grzybekapk.view.fragments.FragStartScreen
import com.example.grzybekapk.view.fragments.FragCalendar
import com.example.grzybekapk.view.fragments.FragMyEvents
import com.example.grzybekapk.view.fragments.FragCreateEvents

class MainScreen : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)


        toolbar = findViewById(R.id.toolbar)                                    // Creating handle for toolbar
        setSupportActionBar(toolbar)                                            // Setting toolbar in activity
        supportActionBar?.setTitle(null)                                        // Deleting "Title" from toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)                           // Setting a button for toolbar

        drawerLayout = findViewById(R.id.drawer_layout)                         // Creating handle for layout with drawer (activity_main_screen.xml)
        val navigationView: NavigationView = findViewById(R.id.navBar)          // Creating handle for pull-out menu
        navigationView.setNavigationItemSelectedListener { menuItem ->          // Managing events for pull-out menu
                menuItem.isChecked = true                                       // Selecting items

                var fragment: Fragment? = null

                when(menuItem.itemId){                                          // Funny switch
                    R.id.moje_wydarzenia -> {                                   // Creating fragments depending on choice
                        fragment = FragMyEvents()

                    }
                    R.id.ekran_glowny ->{
                        fragment = FragStartScreen()
                    }
                    R.id.utworz_wydarzenie ->{
                        fragment = FragCreateEvents()
                    }
                    R.id.kalendarz_but ->{
                        fragment = FragCalendar()
                    }
                }
                                                                                // Replacing fragments
            if(fragment!=null) {
                val man = supportFragmentManager
                val trans = man.beginTransaction()

                trans.replace(R.id.frameLay, fragment)
                trans.commit()
            }

                drawerLayout.closeDrawers()                                     // Closing pull-out menu
                true

        }
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.ekran_glowny)                    // On the beginning "EkranGłówny" is checked
            val fragEkranStartowy = FragStartScreen()                           // Creating fragment for first use
            val manager = supportFragmentManager                                // Fragment transaction
            val transaction = manager.beginTransaction()

            transaction.replace(R.id.frameLay, fragEkranStartowy)               // Replacement
                .commit()                                                       // Commit of your changes
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {   // Strange construction
        android.R.id.home -> {                                                  // Menu's button id
            drawerLayout.openDrawer(GravityCompat.START)                        // Opening pull-out menu
            true
        } else ->{
            super.onOptionsItemSelected(item)
        }
    }
}
