package com.joeblakeb.battleships.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.joeblakeb.battleships.R

/**
 * The initial activity which opens when the app is opened.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonClick = findViewById<Button>(R.id.buttonPlay)
        buttonClick.setOnClickListener {
            val intent = Intent(this, PlaceShipsActivity::class.java)
            startActivity(intent)
        }
    }
}