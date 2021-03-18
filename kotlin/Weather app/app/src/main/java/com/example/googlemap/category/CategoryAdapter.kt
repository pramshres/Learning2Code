package com.example.googlemap.category

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.googlemap.R
import com.example.googlemap.mainAct.MainActivity


class CategoryAdapter (private val context: Context, private val kategorier: List<Kategori>) : BaseAdapter() {


    override fun getCount(): Int {
            return kategorier.size
        }

    override fun getItem(position: Int): Any {
        return kategorier[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    //oppretter View av GridView fra CategoryFrag
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var bw = false
        if (context is MainActivity) {
            bw = (context).getColor()
        }
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val root: View = layoutInflater.inflate(R.layout.element_category, parent, false)

        //oppretter en rute og setter tekst, farge og bilde
        val kat: Kategori = kategorier[position]

        val titleText = root.findViewById(R.id.tittel) as TextView
        val imageSrc = root.findViewById(R.id.image) as ImageView

        if (bw){
            root.setBackgroundResource(R.drawable.radius_bw)
        }
        else {
            root.setBackgroundResource(R.drawable.radius)
        }

        titleText.text = kat.tittel
        imageSrc.setImageResource(kat.bilde)
        return root
    }

}
