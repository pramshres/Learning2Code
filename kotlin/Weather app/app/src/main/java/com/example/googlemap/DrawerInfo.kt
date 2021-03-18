package com.example.googlemap

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_drawer_info.*

class DrawerInfo : AppCompatActivity() {

    //settings
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPref = "mypref"
    private val bakgrunn = "bakgrunn"
    private val storTekst = "storTekst"
    private val fargefilter = "fargefilter"

    @SuppressLint("SetTextI18n")
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
            if (tekst){
                setTheme(R.style.bwBig)
            }
            else {
                setTheme(R.style.bw)
            }
        }
        setContentView(R.layout.activity_drawer_info)
        if (bakg){
            changeBackground(true)
        }
        else {
            changeBackground(false)
        }

        when (intent.getStringExtra("key")) {
            "nyheter" -> {
                overskrift.text = "Nyheter"
                info_drawer.text = "18.05.2020\n\nNy aktivitet: Hobby-flyging\nDrawerlayout oppdatert"
            }
            "info" -> {
                overskrift.text = "Om appen"
                info_drawer.text = "Appens funksjon er å kartlegge og presentere ulike værforhold relevant for å utføre fritidsaktiviteter. \nDet er også mulig å finne ut hvor man kan utføre disse aktivitetene enten nær sin egen posisjon, eller på et valgt punkt"
            }
            "hjelp" -> {
                overskrift.text = "Hjelp"
                info_drawer.text = "Dette er hjelp seksjonen.\nVi kunne trengt litt hjelp med denne!"
            }
        }

    }


    private fun changeBackground(bg: Boolean){
        val someView = findViewById<ConstraintLayout>(R.id.drawer_cont)
        val boks = findViewById<TextView>(R.id.info_drawer)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        if (!bg){
            someView.setBackgroundColor(ContextCompat.getColor(this, R.color.hvit))
            boks.setBackgroundResource(R.drawable.radius_bw)
        }
        else if (bol){
            someView.setBackgroundResource(R.drawable.bw_lang)
            boks.setBackgroundResource(R.drawable.radius_bw)
        }
        else {
            someView.setBackgroundResource(R.drawable.bakgrunn_test)
        }
    }
}
