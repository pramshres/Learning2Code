package com.example.googlemap.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryViewModel: ViewModel() {


    /*Vi instanserer variabler av typen MutableLiveData, siden de er livedata og skal potensielt forandres etter at de blir observert.
    * Dataene kommer fra CategoryListRepository, m singleton pattern. */


    private var catListMut: MutableLiveData<MutableList<Kategori>> = CategoryListRepository.getCategoryList()


    fun getData(): MutableLiveData<MutableList<Kategori>> {

            return catListMut

    }

}


