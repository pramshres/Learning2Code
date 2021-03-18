package com.example.googlemap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.googlemap.mainAct.MainActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash_screen)

        //3second splash time
        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            //finish this activity
            finish()
        }, 3000)

    }


}
