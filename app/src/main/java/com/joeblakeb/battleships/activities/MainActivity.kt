package com.joeblakeb.battleships.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.joeblakeb.battleships.R
import com.joeblakeb.battleships.utils.EXTRA_OTHER_PLAYER
import com.joeblakeb.battleships.utils.OTHER_PLAYER_PROBABILITY
import com.joeblakeb.battleships.utils.OTHER_PLAYER_RANDOM

/**
 * The initial activity which opens when the app is opened.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (pair in listOf(
            Pair(R.id.buttonPlayEasy, OTHER_PLAYER_RANDOM),
            Pair(R.id.buttonPlayHard, OTHER_PLAYER_PROBABILITY)
        )) {
            val button = findViewById<Button>(pair.first)
            button.setOnClickListener {
                val intent = Intent(this, PlaceShipsActivity::class.java)
                intent.putExtra(EXTRA_OTHER_PLAYER, pair.second)
                startActivity(intent)
            }
        }
    }
}