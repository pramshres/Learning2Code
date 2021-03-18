package com.example.googlemap.Temperatur.weatherIconActivity

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.googlemap.R
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

class WeatherIconActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var symbol : String
    private lateinit var toast : Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_icon)

        val weatherIconSpinner = findViewById<Spinner>(R.id.weather_icon_spinner)
        val weatherIconPngButton = findViewById<Button>(R.id.weather_icon_png_button)
        val weatherIconSvgButton = findViewById<Button>(R.id.weather_icon_svg_button)

        weatherIconSpinner.onItemSelectedListener = this

        weatherIconPngButton.setOnClickListener {
            loadWeatherIconPng()
        }

        weatherIconSvgButton.setOnClickListener {
            loadWeatherIconSvg()
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        symbol = parent?.getItemAtPosition(position).toString()
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        toast = Toast.makeText(applicationContext, "Du må velge et symbol!", Toast.LENGTH_LONG)
        toast.show()
    }


    private fun loadWeatherIconPng() {
        val weatherIconSvgImageView = findViewById<ImageView>(R.id.weather_icon_svg_image_view)
        val weatherIconPngImageView = findViewById<ImageView>(R.id.weather_icon_png_image_view)
        val baseURL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/weathericon/1.1"
        val product = "?symbol=" + symbol.split(":")[0] + "&content_type=image%2Fpng"

        weatherIconSvgImageView.setImageResource(android.R.color.transparent)
        Glide.with(this).load("$baseURL$product").into(weatherIconPngImageView)
    }


    private fun loadWeatherIconSvg() {
        val weatherIconSvgImageView = findViewById<ImageView>(R.id.weather_icon_svg_image_view)
        val baseURL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/weathericon/1.1"
        val product = "?symbol=" + symbol.split(":")[0] + "&content_type=image/svg%2Bxml"
        val uri: Uri = Uri.parse("$baseURL$product")

        GlideToVectorYou.justLoadImage(this, uri, weatherIconSvgImageView)
    }

}
