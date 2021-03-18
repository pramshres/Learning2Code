package com.example.googlemap.mainAct

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.example.googlemap.DrawerInfo
import com.example.googlemap.FrontPage.FrontpageFrag
import com.example.googlemap.Kattegori
import com.example.googlemap.R
import com.example.googlemap.Settings
import com.example.googlemap.WeatherForecast.WeatherForecast
import com.example.googlemap.category.CategoryFrag
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var drawer: DrawerLayout
    private lateinit var actionBarDraw: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var pagerAdapter: InternalPagerAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    //settings
    private val sharedPref = "mypref"
    private val bakgrunn = "bakgrunn"
    private val storTekst = "storTekst"
    private val fargefilter = "fargefilter"
    private lateinit var cs: ColorStateList
    private var grey = false

    companion object {
        var staticStedsnavn = ""
    }

    fun getColor():Boolean{
        return grey
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //settings
        sharedPreferences = getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        val tekst = sharedPreferences.getBoolean(storTekst, false)
        val bakg = sharedPreferences.getBoolean(bakgrunn, false)

        if (!bol){
            if (tekst){
                setTheme(R.style.AppThemeBig)
            }
            else {
                setTheme(R.style.AppTheme)
            }
        }
        else {
            grey = true
            if (tekst){
                setTheme(R.style.bwBig)
            }
            else {
                setTheme(R.style.bw)
            }
        }
        setContentView(R.layout.activity_main)
        if (bakg){
            changeBackground(true)
        }
        else {
            changeBackground(false)
        }

        //navigasjonsbar i bunnen
        bottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
      
        drawer = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        pagerAdapter = InternalPagerAdapter(supportFragmentManager)
        pagerAdapter.addFrag(FrontpageFrag.newInstance()) //Hovedskjermen
        pagerAdapter.addFrag(WeatherForecast.newInstance()) //Kart
        pagerAdapter.addFrag(CategoryFrag.newInstance()) //kategori
        view_pager.adapter = pagerAdapter

        //change if black theme
        if (bol){
            val someView = findViewById<FrameLayout>(R.id.frag_container)
            someView.setBackgroundResource(R.drawable.bw_lang)
            bottomNavigationView.setBackgroundResource(R.drawable.drawer_gradient_bw)
            bottomNavigationView.setBackgroundResource(R.drawable.drawer_gradient_bw)


            createStateList()
            bottomNavigationView.itemIconTintList = cs
            bottomNavigationView.itemTextColor = cs

            val navView = findViewById<View>(R.id.nav_view) as NavigationView
            val header = navView.getHeaderView(0)
            val sideNavLayout: LinearLayout = header.findViewById<View>(R.id.iddd) as LinearLayout
            sideNavLayout.setBackgroundResource(R.drawable.bw_topp)
            navView.setBackgroundResource(R.drawable.drawer_gradient_bw)
        }

        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrollStateChanged(state: Int) {


            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                //val selectedScreen = pagerAdapter.getItem(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
      
        actionBarDraw = ActionBarDrawerToggle(
            this,
            drawer,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(actionBarDraw)
        navView.setNavigationItemSelectedListener(this)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            onSharedPreferenceChanged()
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }


    private fun onSharedPreferenceChanged() {
        this.recreate()
    }

    private fun createStateList(){
        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )
        val colors = intArrayOf(
            Color.WHITE,
            Color.GRAY
        )
        cs = ColorStateList(states, colors)
    }


    private fun changeBackground(bg: Boolean){
        val navView = findViewById<View>(R.id.nav_view) as NavigationView
        val header = navView.getHeaderView(0)
        val sideNavLayout: LinearLayout = header.findViewById<View>(R.id.iddd) as LinearLayout
        val someView = findViewById<FrameLayout>(R.id.frag_container)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        if (!bg){
            someView.setBackgroundResource(R.drawable.bg_selector)
            sideNavLayout.setBackgroundResource(R.drawable.drawer_alt_top)
            navView.setBackgroundResource(R.drawable.drawer_alt_top)
        }
        else if (bol){
            someView.setBackgroundResource(R.drawable.bw_lang)
        }
        else {
            someView.setBackgroundResource(R.drawable.bakgrunn_test)
        }
    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId)  {

            R.id.news -> {
                val intent = Intent(this, DrawerInfo::class.java)
                intent.putExtra("key", "nyheter")
                startActivity(intent)
            }
            R.id.secret -> {
                val intent = Intent(this, Kattegori::class.java)
                startActivity(intent)
            }
            R.id.info -> {
                val intent = Intent(this, DrawerInfo::class.java)
                intent.putExtra("key", "info")
                startActivity(intent)
            }
            R.id.help -> {
                val intent = Intent(this, DrawerInfo::class.java)
                intent.putExtra("key", "hjelp")
                startActivity(intent)
            }

            R.id.settings -> {
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
            }

        }

        return true
    }

    override fun onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }


    class InternalPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragList : MutableList<Fragment> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragList[position]
        }

        override fun getCount(): Int {
            return fragList.size
        }

        fun addFrag(frag: Fragment) {
            fragList.add(frag)
        }
    }


    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_home -> {
                view_pager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_map -> {
                view_pager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_category -> {
                view_pager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        supportFragmentManager.beginTransaction().replace(
            R.id.frag_container,
            selectedFragment!!
        ).commit()

        true
    }

}
