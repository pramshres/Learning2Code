package com.example.googlemap.category

import androidx.lifecycle.MutableLiveData
import com.example.googlemap.R

/*Vi kaller dette for et objekt, siden vi vil ha static funksjonalitet siden dette er et singleton pattern*/
object CategoryListRepository {

    private lateinit var categoryList: MutableList<Kategori>

    /*Vi setter, og returnerer til view*/
    fun getCategoryList(): MutableLiveData<MutableList<Kategori>> {
        setCategoryList()
        return MutableLiveData(categoryList)
    }


    /*Setter metode, vi setter hardkodet data til å være categoryList*/
    private fun setCategoryList() {
        categoryList = mutableListOf(
            Kategori("Temperatur", R.drawable.temperatur_ikon_original),
            Kategori("Strender", R.drawable.strender2_ikon_original),
            Kategori("Fiske", R.drawable.fisking_ikon_original),
            Kategori("Padle", R.drawable.padle2_ikon_original),
            Kategori("Fjelltur", R.drawable.fjelltur_ikon_original),
            Kategori("Klatring", R.drawable.klatring_ikon_original),
            Kategori("Turterreng", R.drawable.tur_ikon_original),
            Kategori("Hobby-flyging", R.drawable.flyging_ikon_original),
            Kategori("Coming Soon", R.drawable.tom_ikon_original),
            Kategori("Coming Soon", R.drawable.tom_ikon_original)
        )
    }

}