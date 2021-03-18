package com.example.googlemap.ocationForecast.locationForecast.Langtid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googlemap.R
import com.example.googlemap.ocationForecast.locationForecast.Weather
import kotlinx.android.synthetic.main.element_lang.view.dato
import kotlinx.android.synthetic.main.element_lang.view.icon
import kotlinx.android.synthetic.main.element_lang.view.nedbor
import kotlinx.android.synthetic.main.element_lang.view.temp
import kotlinx.android.synthetic.main.element_lang.view.*

class LangtidsAdapter (private val myDataset: ArrayList<Weather>, c: Context):

    RecyclerView.Adapter<LangtidsAdapter.MyViewHolder>() {
    private val cont = c

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val icon: ImageButton = itemView.icon
        val dato: TextView = itemView.dato
        val tid: TextView = itemView.tid
        val temp: TextView = itemView.temp
        val nedbor: TextView = itemView.nedbor
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.element_lang, parent, false) as View
        return MyViewHolder(
            v
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = myDataset[position]
        val dato = current.visDato()
        holder.dato.text = dato

        val tidFra = current.fra
        val tidTil = current.til
        val tidTotal = "$tidFra - $tidTil"
        holder.tid.text = tidTotal

        val tempTekst = current.minTemp
        val temp = "$tempTekst \u2103"
        holder.temp.text = temp

        val nedborMengde = current.precMin
        val nedbor = "$nedborMengde mm regn"
        holder.nedbor.text=nedbor

        val ikon = current.getIkon()
        holder.icon.setImageResource(android.R.color.transparent)

        Glide.with(cont).load(ikon).dontTransform().into(holder.icon)
    }


    override fun getItemCount() = myDataset.size

}