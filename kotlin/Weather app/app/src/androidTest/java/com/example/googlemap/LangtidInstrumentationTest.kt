package com.example.googlemap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.googlemap.ocationForecast.locationForecast.Korttid.KorttidViewModel
import com.example.googlemap.ocationForecast.locationForecast.Langtid.LangtidViewModel
import com.example.googlemap.ocationForecast.locationForecast.Weather
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LangtidInstrumentationTest {


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var data: ArrayList<Weather> = ArrayList()


    @Test
    fun testOne() {

        val instance = LangtidViewModel()

        instance.getForecastList("59.7607205", "10.0386087").observeForever {

            data = it.value!!

        }


        Assert.assertNotNull(data)

    }

    @Test
    fun assertOutOfBounds() {

        val instance = LangtidViewModel()

        instance.getForecastList("0", "0").observeForever {

            data = it.value!!

        }


        Assert.assertNotNull(data)

    }


}