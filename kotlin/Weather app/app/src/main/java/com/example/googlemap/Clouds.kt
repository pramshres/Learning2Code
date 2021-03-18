package com.example.googlemap

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_clouds.*
import java.text.SimpleDateFormat
import java.util.*


class Clouds : AppCompatActivity() {

    //settings
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPref = "mypref"
    private val bakgrunn = "bakgrunn"
    private val storTekst = "storTekst"
    private val fargefilter = "fargefilter"

    private var teller = 0

    @SuppressLint("SimpleDateFormat")
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
        setContentView(R.layout.activity_clouds)
        if (bakg){
            changeBackground(true)
        }
        else {
            changeBackground(false)
        }



        val urlBase = "https://in2000-apiproxy.ifi.uio.no/weatherapi/routeforecast/1.0/clouds?time=" //2020-03-18T16%3A00%3A00Z


        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        //val df = DateFormat.getTimeInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm")
        df.timeZone = TimeZone.getTimeZone("GMT+2:00")
        val gmtTime = df.format(Date()).toString()

        val splitter = gmtTime.split(" ", ":")
        println(splitter)

        val hourInt = splitter[1].toInt()


        if (hourInt >= 22 || hourInt < 4) {
            if (hourInt >= 22) {
                teller = hourInt-22
            }
            else if(hourInt < 4) {
                teller = hourInt+2
            }
            seek.max = 18-teller
        }
        else if (hourInt >= 16) {
            teller = hourInt-16
            seek.max = 18-teller
        }
        else if (hourInt >= 10) {
        teller = hourInt-10
        seek.max = 18-teller
        }
        else if (hourInt >= 4) {
            teller = hourInt - 4
            seek.max = 18 - teller
        }

        timefelt.text = gmtTime
        seek.visibility = View.INVISIBLE


        val sb = StringBuilder()
        sb.append(urlBase).append(splitter[0]).append("T").append(splitter[1]).append("%3A").append(splitter[2]).append("%3A00Z")
        println(sb.toString())

        val str = sb.toString()


        Glide.with(this)
            .load(str)
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(imageSky)

        seek.visibility = View.VISIBLE

        //mAttacher.update()


        //Informasjon fra de ulike fargeknappene
        yellow.setOnClickListener {
            onClick(yellow)
        }
        light_brown.setOnClickListener {
            onClick(light_brown)
        }
        red.setOnClickListener {
            onClick(red)
        }

        frame.setOnClickListener {
            infoView.visibility = View.INVISIBLE
        }


        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                progressView.text = "Timer : $i+"

                //val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val cal2 = Calendar.getInstance()
                cal2.add(Calendar.HOUR, i)

                val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm")
                sdf2.timeZone = TimeZone.getTimeZone("GMT+2:00")
                val sdf3 = sdf2.format(cal2.time).toString()

                timefelt.text = sdf3

                val parts = sdf3.split(" ", ":")


                println(parts)
                println(parts[0])

                val sb2 = StringBuilder()
                sb2.append(urlBase).append(parts[0]).append("T").append(parts[1]).append("%3A").append(parts[2]).append("%3A00Z")
                val str2 = sb2.toString()


                Glide.with(this@Clouds)
                    .load(str2)
                    .centerCrop()
                    .placeholder(circularProgressDrawable)
                    .into(imageSky)

                //mAttacher.update()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
                Toast.makeText(applicationContext,"start tracking", Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun changeBackground(bg: Boolean){
        val someView = findViewById<ConstraintLayout>(R.id.clouds_main)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        if (!bg){
            someView.setBackgroundResource(R.drawable.drawer_gradient_bw)
        }
        else if (bol){
            someView.setBackgroundResource(R.drawable.bw_lang)
        }
        else {
            someView.setBackgroundResource(R.drawable.gradient)
        }
    }


    private fun onClick(view : View?) {
        when (view?.id) {
            R.id.yellow -> {
                infoView.text = getString(R.string.yellow)
            }
            R.id.light_brown -> {
                infoView.text = getString(R.string.light_brown)
            }
            R.id.red -> {
                infoView.text = getString(R.string.red)
            }
        }
        infoView.visibility = View.VISIBLE
    }

}


