package com.example.grzybekapk.view.aktywnosci

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.MenuItem
import com.example.grzybekapk.R
import com.example.grzybekapk.view.fragmenty.FragEkranStartowy
import com.example.grzybekapk.view.fragmenty.FragKalendarz
import com.example.grzybekapk.view.fragmenty.FragMojeWydarzenia
import com.example.grzybekapk.view.fragmenty.FragUtworzWydarzenie

class EkranGlowny : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekran_glowny)


        toolbar = findViewById(R.id.toolbar)                                    // Stworzenie uchwytu do toolbar'a
        setSupportActionBar(toolbar)                                            // Ustawienie toolbar'a w tej aktywności
        supportActionBar?.setTitle(null)                                        // Usunięcie "Title" z toolbar'a
        toolbar.setNavigationIcon(R.drawable.ic_menu)                           // Wstawienie tego przycisku do toolbar'a

        drawerLayout = findViewById(R.id.drawer_layout)                         // stworzenie uchwytu do layoutu z drawerem (activity_ekran_glowny.xml)
        val navigationView: NavigationView = findViewById(R.id.navBar)          // stworzenie uchwytu do tego wysuwanego menu
        navigationView.setNavigationItemSelectedListener { menuItem ->          // robienie event'ów do bocznego menu
                menuItem.isChecked = true                                       // zaznaczanie item'ów po naciśnięciu

                var fragment: Fragment? = null

                when(menuItem.itemId){                                          // zjebany switch
                    R.id.moje_wydarzenia -> { // obsługa                        // tworzenie fragmentów w zależności co wcisnąłeś
                        fragment = FragMojeWydarzenia()

                    }
                    R.id.ekran_glowny ->{
                        fragment = FragEkranStartowy()
                    }
                    R.id.utworz_wydarzenie ->{
                        fragment = FragUtworzWydarzenie()
                    }
                    R.id.kalendarz_but ->{
                        fragment = FragKalendarz()
                    }
                }
                                                                                // podmiana fragmentów
            if(fragment!=null) {
                val man = supportFragmentManager
                val trans = man.beginTransaction()

                trans.replace(R.id.frameLay, fragment)
                trans.commit()
            }

                drawerLayout.closeDrawers()                                     // zasunięcie bocznego menu
                true

        }
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.ekran_glowny)                    // na początek w menu bocznym zaznaczony będzie ekran główny
            val fragEkranStartowy = FragEkranStartowy()                         // stworzenie fragmentu dla ekranu startowego
            val manager = supportFragmentManager                                // dalej jest obsługa transakcji co podmienia fragment
            val transaction = manager.beginTransaction()

            transaction.replace(R.id.frameLay, fragEkranStartowy)               // dokładnie w tym miejscu się podmienia
                .commit()                                                       // a tak właściwie to tu, bo tu się zatwierdzają zmiany
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {   // nie wiem co to za pojebana konstrukcja, ten "when" to switch, a czemu tam jest "=" to ja nie wiem xD
        android.R.id.home -> {                                                  // to jest id przycisku na toolbarze
            drawerLayout.openDrawer(GravityCompat.START)                        // to jest odpowiedzialne za wysunięcie bocznego menu
            true
        } else ->{                                                              // obsłużenie reszty
            super.onOptionsItemSelected(item)
        }
    }


}
