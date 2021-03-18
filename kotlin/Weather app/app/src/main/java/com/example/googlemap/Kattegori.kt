package com.example.googlemap

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_kattegori.*
import java.util.*

class Kattegori : AppCompatActivity() {

    private var topScore = 0
    private var buttomScore = 0
    private var currentTopCard = 0
    private var currentBottomCard = 0
    private var currentWinnerText : String = ""

    //settings
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPref = "mypref"
    private val bakgrunn = "bakgrunn"
    private val storTekst = "storTekst"
    private val fargefilter = "fargefilter"

    //MÅ GJØRES
    //1. Fikse oppsettet under rotasjon
    //2. Kanskje gjøre dette til fragment slik at activity ikke restarter hver gang man roterer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //settings
        sharedPreferences = getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        val tekst = sharedPreferences.getBoolean(storTekst, false)
        val bakg = sharedPreferences.getBoolean(bakgrunn, false)
        if (!bol){
            if (tekst){
                setTheme(R.style.AppThemeBig)
            }
            else {
                setTheme(R.style.AppTheme)
            }
        }
        else {
            if (tekst){
                setTheme(R.style.bwBig)
            }
            else {
                setTheme(R.style.bw)
            }
        }
        setContentView(R.layout.activity_kattegori)
        if (bakg){
            changeBackground(true)
        }
        else {
            changeBackground(false)
        }


        val cardTop = findViewById<ImageView>(R.id.iv_card_top)
        val cardButtom = findViewById<ImageView>(R.id.iv_card_bottom)
        val youScore = findViewById<TextView>(R.id.counter)
        val enemyScore= findViewById<TextView>(R.id.enemy_counter)
        val drawBtn = findViewById<Button>(R.id.draw)

        enemyScore.text = topScore.toString()
        youScore.text = buttomScore.toString()

        currentTopCard = resources.getIdentifier("c_back", "drawable", packageName)
        currentBottomCard = resources.getIdentifier("c_back", "drawable", packageName)

        val r = Random()

        drawBtn.setOnClickListener {
            val topCard = r.nextInt(13) + 2 //this is for cards 2-14
            val buttomCard = r.nextInt(13) + 2 //this is for cards 2-14


            //display images
            val topImage = resources.getIdentifier("c$topCard", "drawable", packageName)
            cardTop.setImageResource(topImage)
            currentTopCard = topImage

            val buttomImage = resources.getIdentifier("c$buttomCard", "drawable", packageName)
            cardButtom.setImageResource(buttomImage)
            currentBottomCard = buttomImage

            //compare cards, add points and display them
            when {
                topCard > buttomCard -> {
                    topScore++
                    enemyScore.text = topScore.toString()
                    opponent.setTextColor(Color.GREEN)
                    you.setTextColor(Color.WHITE)
                    currentWinnerText = "opponent"

                }
                topCard < buttomCard -> {
                    buttomScore++
                    youScore.text = buttomScore.toString()
                    you.setTextColor(Color.GREEN)
                    opponent.setTextColor(Color.WHITE)
                    currentWinnerText = "you"
                }
                else -> {
                    Toast.makeText(this, "War", Toast.LENGTH_SHORT).show()
                    opponent.setTextColor(Color.WHITE)
                    you.setTextColor(Color.WHITE)
                    currentWinnerText = "both"
                }
            }

        }
        if (savedInstanceState != null) {
            topScore = savedInstanceState.getInt("youScore")
            buttomScore = savedInstanceState.getInt("enemyScore")

            youScore.text = buttomScore.toString()
            enemyScore.text = topScore.toString()

            currentTopCard = savedInstanceState.getInt("topImage")
            currentBottomCard = savedInstanceState.getInt("botImage")

            iv_card_top.setImageResource(currentTopCard)
            iv_card_bottom.setImageResource(currentBottomCard)

            currentWinnerText = savedInstanceState.getString("winnerText").toString()
            when (currentWinnerText) {
                "you" -> {
                    you.setTextColor(Color.GREEN)
                }
                "opponent" -> {
                    opponent.setTextColor(Color.GREEN)
                }
                "both" -> {
                    opponent.setTextColor(Color.WHITE)
                    you.setTextColor(Color.WHITE)
                }
            }
        }
    }
    override fun onSaveInstanceState(outState : Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("youScore", topScore)
        outState.putInt("enemyScore", buttomScore)
        outState.putInt("topImage", currentTopCard)
        outState.putInt("botImage", currentBottomCard)
        outState.putString("winnerText", currentWinnerText)
    }

    private fun changeBackground(bg: Boolean){
        val someView = findViewById<ConstraintLayout>(R.id.katt_id)
        val boks = findViewById<Button>(R.id.draw)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        if (!bg){
            someView.setBackgroundResource(R.drawable.drawer_gradient_bw)
            boks.setBackgroundResource(R.drawable.drawer_gradient_bw)
        }
        else if (bol){
            someView.setBackgroundResource(R.drawable.bw_lang)
            boks.setBackgroundResource(R.drawable.drawer_gradient_bw)
        }
        else {
            someView.setBackgroundResource(R.drawable.bakgrunn_test)
        }
    }

}
