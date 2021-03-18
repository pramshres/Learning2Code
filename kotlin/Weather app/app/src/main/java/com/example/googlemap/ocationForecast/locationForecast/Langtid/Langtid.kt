package com.example.googlemap.ocationForecast.locationForecast.Langtid

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemap.R
import com.example.googlemap.ocationForecast.locationForecast.*

class Langtid : AppCompatActivity() {


    private val langtidVM: LangtidViewModel by viewModels()
    private var lat = ""
    private var lon = ""
    private var stedsnavn = ""
    private var langtid: ArrayList<Weather> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_langtid)
        val tittel = findViewById<TextView>(R.id.tittel)

        //posisjon
        val mIntent = intent
        val latlong = mIntent.extras!!.getStringArrayList("latlong")
        if (latlong != null) {
            lat = latlong[0]
            lon = latlong[1]
            stedsnavn = latlong[2]

            val midl = stedsnavn.toCharArray()
            var nyttNavn = ""
            var komma = 0
            for (char in midl) {
                if (char == ',') {
                    komma++
                    if (komma >= 2) {
                        break
                    }
                }
                nyttNavn += char
            }
            stedsnavn = nyttNavn
            tittel.text = stedsnavn

            langtidVM.getForecastList(lat, lon).observe(this, Observer{

                langtid = it.value!!

                runOnUiThread {
                    makeTheView()
                }

            })
        } else {
            Toast.makeText(this, "Feil! -> Intent", Toast.LENGTH_SHORT).show()
        }
    }


    private fun makeTheView() {
        val viewManager = LinearLayoutManager(this@Langtid)
        val viewAdapter =
            LangtidsAdapter(
                langtid,
                this@Langtid
            )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewLang).apply {
            setHasFixedSize(true)
        }

        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
    }

}