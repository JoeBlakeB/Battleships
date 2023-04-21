package com.joeblakeb.battleships.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.joeblakeb.battleships.R
import com.joeblakeb.battleships.utils.OpponentParcelable
import com.joeblakeb.battleships.utils.PARCELABLE_OPPONENT
import com.joeblakeb.battleships.utils.getParcelableCompat
import com.joeblakeb.battleships.views.PlacementGameBoardView

class PlaceShipsActivity : AppCompatActivity() {
    private lateinit var confirmButton: Button
    private lateinit var placementGameBoardView: PlacementGameBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_ships)

        confirmButton = findViewById<Button>(R.id.buttonConfirm)
        placementGameBoardView = findViewById<PlacementGameBoardView>(R.id.placementGameBoardView)

        confirmButton.setOnClickListener { confirmPlacement() }

        if (savedInstanceState != null) {
            placementGameBoardView.gameBoard.opponent = savedInstanceState.getParcelableCompat<OpponentParcelable>(PARCELABLE_OPPONENT)!!.opponent
        }
    }

    /**
     * Start the gameplay activity with the players ship placement.
     */
    private fun confirmPlacement() {
        val intent = Intent(this, GameplayActivity::class.java)
        intent.putExtra(PARCELABLE_OPPONENT, OpponentParcelable(placementGameBoardView.gameBoard.opponent))
        startActivity(intent)
    }

    /**
     * Save the placement game board when the screen rotates so state is not lost.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(PARCELABLE_OPPONENT, OpponentParcelable(placementGameBoardView.gameBoard.opponent))
    }
}