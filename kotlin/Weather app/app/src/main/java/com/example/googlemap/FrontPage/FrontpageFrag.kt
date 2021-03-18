package com.example.googlemap.FrontPage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.googlemap.R
import com.example.googlemap.mainAct.MainActivity.Companion.staticStedsnavn
import com.example.googlemap.ocationForecast.locationForecast.*
import com.example.googlemap.ocationForecast.locationForecast.Korttid.Korttid
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.frontpage_frag_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private val handler = Handler()

class FrontpageFrag: Fragment() {

    private val fpVM: FrontpageFragViewModel by viewModels()
    private var lat : Double = 0.0 //blir initialisert i getPosAndAddress()
    private var long : Double = 0.0 //blir initialisert i getPosAndAddress()
    private lateinit var dag : List<String>
    private var timeForTime: ArrayList<Weather> = ArrayList()
    private var addresse = ""


    companion object {

        fun newInstance(): FrontpageFrag {

            return FrontpageFrag()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addresse = getPosAndAddress() //henter addresse, lat og long
        return inflater.inflate(R.layout.frontpage_frag_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stedView.text = addresse.substringBefore(",")
        staticStedsnavn = addresse //for at CategoryFrag kan hente ut verdien

        //Oppdaterer tid hvert sekund
        handler.post(object : Runnable {
            override fun run() {
                if (dateTime != null) {
                    updateTime()
                }
                //Keep the postDelayed before the updateTime(), so when the event ends, the handler will stop too
                handler.postDelayed(this, 1000)
            }
        })

        //Time for time visning
        time_for_time.setOnClickListener {
            val sendThis = arrayListOf(lat.toString(), long.toString(), addresse)
            val intent = Intent(activity, Korttid::class.java)
            intent.putStringArrayListExtra("latlong", sendThis)
            startActivity(intent)
        }

        //Kall for å finne temperaturenr
        fpVM.fetchTemp(lat.toString(),long.toString()).observe(viewLifecycleOwner, Observer{

            timeForTime = it.value!!
            setTemp()

        })

        //Brukes til å hente sidemenyen med en knapp
        val drawer = activity?.findViewById<DrawerLayout>(R.id.drawer_layout)
        meny_hent.setOnClickListener {
            drawer?.openDrawer(GravityCompat.START)
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun updateTime() {
        val sdf = SimpleDateFormat("EEEE HH:mm")
        sdf.timeZone = TimeZone.getTimeZone("GMT+2:00")
        val dayOfTheWeek = sdf.format(Date()).toString()
        val splittet = dayOfTheWeek.split(" ")
        dag = splittet[0].chunked(3)

        val tidVisning = dag[0].plus(", ").plus(splittet[1])
        dateTime.text = tidVisning

    }


    //forsøker å finne enhetens posisjon
    private fun getPosAndAddress(): String{

        //sjekker om man har location-tillatelse
        if(ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this.context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                com.example.googlemap.category.LOCATION_PERMISSION_REQUEST_CODE
            )
            return "Fant ikke din addresse"
        }

        //finner enhetens/emulatorens posisjon
        val lm = this.context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: return ""
        val longitude: Double = location.longitude
        val latitude: Double = location.latitude

        long = longitude
        lat = latitude

        fpVM.getAddressCall(LatLng(lat, long), requireContext()).observe(viewLifecycleOwner, Observer{

                setAddresse(it.value!!)
                addresse = it.value!!
        })
        return addresse
    }


    //henter temperatur
    private fun setTemp() {

        temp.text = timeForTime[1].temperatureValue
        temp.append("°")
    }


    //presenterer stedsnavn
    private fun setAddresse(stedsnavn : String?) {

        if (stedsnavn == null) {

            CoroutineScope(Dispatchers.Main).launch {
                stedView.text = getString(R.string.no_address)
            }

        } else {

            CoroutineScope(Dispatchers.Main).launch {
                stedView.text = stedsnavn
            }
        }
    }

}