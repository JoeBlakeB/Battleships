package com.joeblakeb.battleships.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.joeblakeb.battleships.R

class PlaceShipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_ships)

        val buttonClick = findViewById<Button>(R.id.buttonConfirm)
        buttonClick.setOnClickListener {
            val intent = Intent(this, GameplayActivity::class.java)
            startActivity(intent)
        }
    }
}