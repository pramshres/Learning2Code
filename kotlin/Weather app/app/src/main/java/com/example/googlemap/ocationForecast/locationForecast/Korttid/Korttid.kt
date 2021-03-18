package com.example.googlemap.ocationForecast.locationForecast.Korttid

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemap.*
import com.example.googlemap.ocationForecast.locationForecast.*


class Korttid : AppCompatActivity() {

    private val korttidVM: KorttidViewModel by viewModels()
    private var lat = ""
    private var long = ""
    private var stedsnavn = ""
    private var timeForTime: ArrayList<Weather> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_for_time)
        val tittel = findViewById<TextView>(R.id.tittel)

        //posisjon
        val mIntent = intent
        val latlong = mIntent.extras!!.getStringArrayList("latlong")
        if (latlong != null){
            lat = latlong[0]
            long = latlong[1]
            stedsnavn = latlong[2]

            val midl = stedsnavn.toCharArray()
            var nyttNavn = ""
            var komma = 0
            for (char in midl){
                if (char == ','){
                    komma++
                    if (komma >= 2){
                        break
                    }
                }
                nyttNavn += char
            }
            stedsnavn = nyttNavn
            tittel.text = stedsnavn

            korttidVM.getForecastList(lat, long).observe(this, Observer{

                timeForTime = it.value!!

                runOnUiThread {
                    makeTheView()
                }
            })
        }
        else {
            Toast.makeText(this, "Feil! -> Intent", Toast.LENGTH_SHORT).show()
        }
    }


    private fun makeTheView() {
        val viewManager = LinearLayoutManager(this@Korttid)

        val viewAdapter = KorttidAdapter(timeForTime, this@Korttid)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
        }

        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
    }

}