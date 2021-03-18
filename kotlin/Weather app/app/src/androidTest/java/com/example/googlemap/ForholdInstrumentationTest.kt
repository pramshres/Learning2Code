package com.example.googlemap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.googlemap.Temperatur.forhold.ForholdViewModel
import com.example.googlemap.nowcast.met.Time
import com.example.googlemap.ocationForecast.locationForecast.Korttid.KorttidViewModel
import com.example.googlemap.ocationForecast.locationForecast.Weather
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ForholdInstrumentationTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private var data: List<Time> = emptyList()
    private var ikonString = ""



    @Test
    fun assertValidIcon() {

        val instance = ForholdViewModel()

        instance.getIkonFromLatLon("59.7607205", "10.0386087").observeForever {

            ikonString = it.value!!

        }


        Assert.assertNotNull(ikonString)

    }

    @Test
    fun assertInvalidIkon() {

        val instance = ForholdViewModel()

        instance.getIkonFromLatLon("0", "0").observeForever {

            ikonString = it.value!!

        }


        Assert.assertEquals(ikonString, "")

    }

    @Test
    fun assertOutOfBounds() {

        val instance = ForholdViewModel()

        instance.getTimeList("0", "0").observeForever {

            data = it.value!!

        }


        Assert.assertNotNull(data)

    }

    @Test
    fun assertReferenceCoordinates() {

        val instance = ForholdViewModel()

        instance.getTimeList("59.7607205", "10.0386087").observeForever {

            data = it.value!!

        }


        Assert.assertNotNull(data)

    }


}