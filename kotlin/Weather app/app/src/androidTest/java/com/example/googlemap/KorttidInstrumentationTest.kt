package com.example.googlemap


import org.junit.Assert
import org.junit.Rule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.googlemap.ocationForecast.locationForecast.Korttid.KorttidViewModel
import com.example.googlemap.ocationForecast.locationForecast.Weather
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class KorttidInstrumentationTest {




    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private var data: ArrayList<Weather> = ArrayList()



    @Test
    fun testOne() {

        val instance = KorttidViewModel()

        instance.getForecastList("59.7607205", "10.0386087").observeForever {

            data = it.value!!

        }


        Assert.assertNotNull(data)

    }

    @Test
    fun assertOutOfBounds() {

        val instance = KorttidViewModel()

        instance.getForecastList("0", "0").observeForever {

            data = it.value!!

        }


        Assert.assertNotNull(data)

    }


}