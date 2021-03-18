package com.example.googlemap.Temperatur.forhold

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.googlemap.R
import com.example.googlemap.nowcast.met.Time
import com.example.googlemap.ocationForecast.locationForecast.Korttid.Korttid
import com.example.googlemap.ocationForecast.locationForecast.Langtid.Langtid
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_forhold.*


class Forhold : AppCompatActivity() {

    //settings
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPref = "mypref"
    private val bakgrunn = "bakgrunn"
    private val storTekst = "storTekst"
    private val fargefilter = "fargefilter"

    private val forholdVM: ForholdViewModel by viewModels()
    //til nowcast-grafen
    private lateinit var graph: GraphView
    private  var timeList: List<Time>? = null

    private var pos = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //settings
        sharedPreferences = getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        val tekst = sharedPreferences.getBoolean(storTekst, false)
        val bakg = sharedPreferences.getBoolean(bakgrunn, false)

        //bakgrunn, farge og tekststørrelse
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
        setContentView(R.layout.activity_forhold)
        if (bakg){
            changeBackground(true)
        }
        else {
            changeBackground(false)
        }

        //henter posisjon fra intent og legger det til text-viewet
        val sted = findViewById<TextView>(R.id.lokasjon)
        val koordinaterSted = findViewById<TextView>(R.id.koordinater)
        var lat = ""
        var lon = ""

        val mIntent = intent
        val latlong = mIntent.extras!!.getStringArrayList("latlong")
        if (latlong != null) {
            lat = latlong[0]
            lon = latlong[1]
            pos = latlong[2]
            val midl = pos.toCharArray()
            var nyttNavn = ""
            var komma = 0
            for (char in midl){
                if (char == ','){
                    komma++
                    if (komma >= 2){
                        break
                    }
                }
                nyttNavn+=char
            }
            pos = nyttNavn
            sted.text = pos

            val temp = lat.take(7).plus(", ").plus(lon.take(7))
            koordinaterSted.text = temp
        }


        //knapper for korttids- og langtids-vær
        val timeForTime = findViewById<Button>(R.id.korttid)
        timeForTime.setOnClickListener{
            val intent = Intent(this, Korttid::class.java)
            intent.putStringArrayListExtra("latlong", latlong)
            startActivity(intent)
        }
        val langtid = findViewById<Button>(R.id.langtid)
        langtid.setOnClickListener{
            val intent = Intent(this, Langtid::class.java)
            intent.putStringArrayListExtra("latlong", latlong)
            startActivity(intent)
        }


        //modifiserer utseendet til regngrafen
        graph = findViewById(R.id.nowcast_graph)
        graph.title = "Nedbør i mm. de neste 80 minuttene"
        //graph.gridLabelRenderer.verticalAxisTitle = "mm.h"
        graph.gridLabelRenderer.horizontalAxisTitle = "min"


        forholdVM.getTimeList(lat, lon).observe(this, Observer{

            timeList = it.value
            initiateGraph(timeList)

        })


        //henter værikon
        forholdVM.getIkonFromLatLon(lat, lon).observe(this, Observer{

            loadImageIntoView(it.value!!)

        })

    }


    //presenterer mengden regn de neste 90 minuttene
    private fun regnOverTid(list: List<Time>) {

        //serie av punkter(målinger), ett punkt hvert 5 minutt
        val series = LineGraphSeries<DataPoint>()
        series.isDrawBackground = true

        var nullRegn = 0


        //sjekker om det regner de neste 90 minuttene, vises med en graf
        for (i in list.indices) {
            val klokkeOriginal = 5 * i
            val regnmengde = list[i].location?.precipitation?.value.toString()

            //øker en teller om det ikke regner
            if (regnmengde == "0.0") {
                nullRegn++
            }

            //oppretter et datapunkt for klokkeslettet og legger det til grafen
            val point = DataPoint(klokkeOriginal.toDouble(), regnmengde.toDouble())
            series.appendData(point, true, list.size)
        }

        //om det ikke er noe regn vises ikke grafen
        if (nullRegn == 23) {

            runOnUiThread {
                nowcast_graph.visibility = View.INVISIBLE
            }
        }
        else {
            graph.addSeries(series)
        }
    }


    //henter værikon for posisjonen
    private fun loadWeatherIconSvg(symbol: String): Uri {
        val baseURL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/weathericon/1.1"
        val product = "?symbol=$symbol&content_type=image/svg%2Bxml"
        return Uri.parse("$baseURL$product")
    }


    private fun loadImageIntoView(ikon: String) {

        val weatherIconSvg = findViewById<ImageView>(R.id.weather_icon_svg)
        weatherIconSvg.setImageResource(android.R.color.transparent)
        val uri = loadWeatherIconSvg(ikon)
        GlideToVectorYou.justLoadImage(this@Forhold, uri, weatherIconSvg)
        Glide.with(this@Forhold).load(uri.toString()).dontTransform().into(weatherIconSvg)
    }


    private fun initiateGraph(timeList: List<Time>?) {

        if (timeList.isNullOrEmpty()) {

            runOnUiThread {
                val msg = "Det finnes ikke nedbørsdata for denne lokasjonen"
                nowcast_graph.visibility = View.INVISIBLE
                val toast: Toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                toast.show()
            }

        } else {

            regnOverTid(timeList)
        }

    }


    //settings
    private fun changeBackground(bg: Boolean){
        val someView = findViewById<ConstraintLayout>(R.id.forh)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        if (!bg){
            someView.setBackgroundResource(R.drawable.drawer_gradient_bw)
        }
        else if (bol){
            someView.setBackgroundResource(R.drawable.bw_lang)
        }
        else {
            someView.setBackgroundResource(R.drawable.bg_selector)
        }
    }

}