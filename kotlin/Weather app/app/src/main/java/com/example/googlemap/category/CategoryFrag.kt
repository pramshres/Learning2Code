package com.example.googlemap.category

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.googlemap.Clouds
import com.example.googlemap.mainAct.MainActivity.Companion.staticStedsnavn
import com.example.googlemap.ocationForecast.nearby.BeachActivity
import com.example.googlemap.ocationForecast.met.OcationForecast
import com.example.googlemap.R
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


const val LOCATION_PERMISSION_REQUEST_CODE = 1

class CategoryFrag : Fragment(), CoroutineScope by MainScope() {

    private val catViewModel: CategoryViewModel by viewModels()
    private lateinit var catAdapter: CategoryAdapter


    /*

    Dette er det samme som å si static i java. Vi har alltid en instans klar av denne klassen til å returneres for PagerAdapter i mainactivity

     */
    companion object {

        fun newInstance(): CategoryFrag {

            return CategoryFrag()
        }

    }

    /*

    Her iflater vi viewet når det er tid for å lage UI'en. Denne kjøres etter onCreate.

     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_category, container, false)
    }

    /*

    Etter at vi har et view, kjøres denne (Denne kjøres etter onCreateView) Her kan du gjøre de vanlige tingene du vil gjøre.
    Merk at context er gitt ved this.context (kotlin synthetic property, er faktisk this.getcontext()

     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridView = view.findViewById<GridView>(R.id.category_grid)

        gridView.adapter = catAdapter

        //asyknront kall for å ikke ha for mye i main-thread
        val future = doAsync {
            uiThread {
                category_pos.text = staticStedsnavn.substringBefore(",")
            }
        }


        //når man trykker på en rute åpnes riktig kategori
        gridView.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->

            if (position == 0) {
                val intent = Intent(this.context, OcationForecast::class.java)
                startActivity(intent)
            }
            if (position == 1) {

                val intent = Intent(this.context, BeachActivity::class.java)
                intent.putStringArrayListExtra("keyword", arrayListOf("beach", "0"))
                startActivity(intent)
            }
            if (position == 2) {

                val intent = Intent(this.context, BeachActivity::class.java)
                intent.putStringArrayListExtra("keyword", arrayListOf("lake", "1"))
                startActivity(intent)
            }
            if (position == 3) {

                val intent = Intent(this.context, BeachActivity::class.java)
                intent.putStringArrayListExtra("keyword", arrayListOf("kayak", "2"))
                startActivity(intent)
            }
            if (position == 4) {

                val intent = Intent(this.context, BeachActivity::class.java)
                intent.putStringArrayListExtra("keyword", arrayListOf("mountain", "3"))
                startActivity(intent)
            }
            if (position == 5) {

                val intent = Intent(this.context, BeachActivity::class.java)
                intent.putStringArrayListExtra("keyword", arrayListOf("climbing", "4"))
                startActivity(intent)
            }
            if (position == 6) {

                val intent = Intent(this.context, BeachActivity::class.java)
                intent.putStringArrayListExtra("keyword", arrayListOf("hiking", "5"))
                startActivity(intent)
            }
            if (position == 7) {

                val intent = Intent(this.context, Clouds::class.java)
                startActivity(intent)
            }

        }
        future.cancel(false)
    }

    /*Kotlin klager om noe som ikke er feil. Denne suppresser warningen siden den forhindrer compile*/
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /*Vi observerer data i viewModel klassen, dataene forandrer seg aldri, så observer har ingen spesifik ting i lambda funksjonen.
        Den trenger bare å være der slik at den gjør init'en sin*/
        catViewModel.getData().observe(this, Observer{})
        val mCategoryList : MutableList<Kategori> = catViewModel.getData().value!!
        catAdapter = CategoryAdapter(this.context!!, mCategoryList)

    }

}