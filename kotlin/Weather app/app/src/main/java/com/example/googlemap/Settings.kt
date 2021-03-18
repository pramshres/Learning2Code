package com.example.googlemap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.googlemap.mainAct.MainActivity


class Settings : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listener: OnSharedPreferenceChangeListener
    private val sharedPref = "mypref"
    private val bakgrunn = "bakgrunn"
    private val storTekst = "storTekst"
    private val fargefilter = "fargefilter"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        val tekst = sharedPreferences.getBoolean(storTekst, false)

        if (!bol){
            if (tekst){
                setTheme(R.style.AppThemeBig)
            }
            else {
                setTheme(R.style.AppTheme)
            }
        }
        else {
            if (tekst){
                setTheme(R.style.bwBig)
            }
            else {
                setTheme(R.style.bw)
            }
        }
        setContentView(R.layout.settings_activity)
        //change if black theme
        if (bol) {
            val someView = findViewById<LinearLayout>(R.id.linearBack)
            someView.setBackgroundResource(R.drawable.bw_lang)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        listener = OnSharedPreferenceChangeListener { _, key ->
            onSharedPreferenceChanged(key)
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)


    }


    //oppdater view hvis bruker gjør en endring
    private fun onSharedPreferenceChanged(key: String?) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        if (key == bakgrunn) {
            val bol = sharedPreferences.getBoolean(bakgrunn, false)
            if (bol){
                editor.putBoolean(bakgrunn, false)
                changeBackground(false)
            }
            else {
                editor.putBoolean(bakgrunn, true)
                changeBackground(true)
            }
        }
        if (key == fargefilter) {
            val bol = sharedPreferences.getBoolean(fargefilter, false)
            val tekst = sharedPreferences.getBoolean(storTekst, false)
            if (bol){
                editor.putBoolean(fargefilter, false)
                if (tekst){
                    changeTheme(bg = true, txt = true)
                }
                else {
                    changeTheme(bg = true, txt = false)
                }
            }
            else {
                editor.putBoolean(fargefilter, true)
                if (tekst){
                    changeTheme(bg = false, txt = true)
                }
                else {
                    changeTheme(bg = false, txt = false)
                }
            }
        }
        if (key == storTekst) {
            val bol = sharedPreferences.getBoolean(fargefilter, false)
            val tekst = sharedPreferences.getBoolean(storTekst, false)
            if (bol){
                if (tekst){
                    editor.putBoolean(storTekst, false)
                    changeTheme(bg = true, txt = true)
                }
                else {
                    editor.putBoolean(storTekst, true)
                    changeTheme(bg = true, txt = false)
                }
            }
            else {
                if (tekst){
                    editor.putBoolean(storTekst, false)
                    changeTheme(bg = false, txt = true)
                }
                else {
                    editor.putBoolean(storTekst, true)
                    changeTheme(bg = false, txt = false)
                }
            }
        }
        editor.apply()
    }


    //hent innstillinger når åpner
    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(listener)
        val bol = sharedPreferences.getBoolean(bakgrunn, false)
        //val tekst = sharedPreferences.getBoolean(FARGEFILTER, false)
        if (bol){
            changeBackground(true)
        }
        else {
            changeBackground(false)
        }
    }


    override fun onStop() {
        super.onStop()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(listener)
    }


    //restart, så endringer gjør seg gjeldende
    fun restartNow(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        finishAffinity()
    }


    //endre bakgrunn
     private fun changeBackground(bg: Boolean){
        val someView = findViewById<LinearLayout>(R.id.linearBack)
         val bol = sharedPreferences.getBoolean(fargefilter, false)
        if (!bg){
            someView.setBackgroundResource(R.drawable.lightgrey)
        }
        else if (bol){
            someView.setBackgroundResource(R.drawable.bw_lang)
        }
        else {
            someView.setBackgroundResource(R.drawable.bakgrunn_test)
        }
    }


    //metode for å endre theme
    private fun changeTheme(bg: Boolean, txt: Boolean){
        val someView = findViewById<LinearLayout>(R.id.linearBack)
        if (!bg) {
            if (!txt){
                setTheme(R.style.AppTheme)
            }
            else {
                setTheme(R.style.AppThemeBig)
            }
            this.recreate()
        }
        else {
            if (!txt){
                someView.setBackgroundResource(R.drawable.bw_lang)
                setTheme(R.style.bw)
            }
            else {
                someView.setBackgroundResource(R.drawable.bw_lang)
                setTheme(R.style.bwBig)
            }
            this.recreate()
        }
    }


    //fragment som heller bruker xml fra root
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

}