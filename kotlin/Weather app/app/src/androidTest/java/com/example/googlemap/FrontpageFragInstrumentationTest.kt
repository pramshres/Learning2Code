package com.example.googlemap

import android.app.Instrumentation
import android.util.Log
import com.example.googlemap.FrontPage.FrontpageFragViewModel
import org.junit.Assert
import org.junit.Rule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.googlemap.ocationForecast.locationForecast.Weather
import com.google.android.gms.maps.model.LatLng
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FrontpageFragInstrumentationTest {


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    private var data: ArrayList<Weather> = ArrayList()

    @Test
    fun assertNormalCoordinates() {

        val instance = FrontpageFragViewModel()


        instance.fetchTemp("59.7607205", "10.0386087").observeForever {

            data = it.value!!

        }

        Assert.assertNotNull(data)
    }



    @Test
    fun assertOutOfBounds() {

        val instance = FrontpageFragViewModel()

        instance.fetchTemp("0", "0").observeForever {

            data = it.value!!

        }


        Assert.assertNotNull(data)
    }

}