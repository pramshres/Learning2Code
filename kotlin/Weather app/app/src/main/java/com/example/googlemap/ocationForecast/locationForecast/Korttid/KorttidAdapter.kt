package com.example.googlemap.ocationForecast.locationForecast.Korttid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googlemap.R
import com.example.googlemap.ocationForecast.locationForecast.Weather
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.element.view.*
import kotlin.collections.ArrayList

class KorttidAdapter (private val myDataset: ArrayList<Weather>, c: Context):

    RecyclerView.Adapter<KorttidAdapter.MyViewHolder>() {
    private val cont = c

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val icon: ImageButton = itemView.icon
        val dato: TextView = itemView.dato
        val temp: TextView = itemView.temp
        val nedbor: TextView = itemView.nedbor
        val vind: TextView = itemView.vind
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.element, parent, false) as View
        return MyViewHolder(
            v
        )
    }


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = myDataset[position]
        val dato = current.getPenDato()
        holder.dato.text = dato

        val tempTekst = current.temperatureValue
        val temp = "Temperatur: $tempTekst \u2103"
        holder.temp.text = temp

        val nedborMengde = current.precipitationValue
        val nedborEnhet = current.precipitationUnit
        val nedbor = "Nedbør: $nedborMengde $nedborEnhet"
        holder.nedbor.text = nedbor

        val vindNavn = current.windSpeedName
        val vindspeed = current.windSpeedMps
        val vindRetning = current.getVindRetning()
        val vind = "$vindNavn: $vindspeed m/s fra $vindRetning"
        holder.vind.text = vind

        val ikon = current.getIkon()
        holder.icon.setImageResource(android.R.color.transparent)
        val uri: Uri = Uri.parse(ikon)
        GlideToVectorYou.justLoadImage(cont as Activity?, uri, holder.icon)

        Glide.with(cont).load(ikon).dontTransform().into(holder.icon)
    }


    override fun getItemCount() = myDataset.size

}