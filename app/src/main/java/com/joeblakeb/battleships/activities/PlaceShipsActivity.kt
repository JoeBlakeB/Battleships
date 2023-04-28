package com.joeblakeb.battleships.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.joeblakeb.battleshipgame.MutableOpponent
import com.joeblakeb.battleships.R
import com.joeblakeb.battleships.utils.EXTRA_OTHER_PLAYER
import com.joeblakeb.battleships.utils.OpponentParcelable
import com.joeblakeb.battleships.utils.EXTRA_SHIP_PLACEMENT
import com.joeblakeb.battleships.utils.OTHER_PLAYER_RANDOM
import com.joeblakeb.battleships.utils.getParcelableCompat
import com.joeblakeb.battleships.views.PlacementGameBoardView

/**
 * The activity to allow the player to select where
 * they want to place their ships.
 */
class PlaceShipsActivity : AppCompatActivity() {
    private lateinit var confirmButton: Button
    private lateinit var placementGameBoardView: PlacementGameBoardView

    private var selectedOtherPlayer: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_ships)

        confirmButton = findViewById(R.id.buttonConfirm)
        placementGameBoardView = findViewById(R.id.placementGameBoardView)

        confirmButton.setOnClickListener { confirmPlacement() }

        selectedOtherPlayer = intent.getIntExtra(EXTRA_OTHER_PLAYER, OTHER_PLAYER_RANDOM)

        if (savedInstanceState != null) {
            placementGameBoardView.mutableOpponent = MutableOpponent(
                savedInstanceState.getParcelableCompat<OpponentParcelable>(EXTRA_SHIP_PLACEMENT)!!)
        }
    }

    /**
     * Start the gameplay activity with the players ship placement.
     */
    private fun confirmPlacement() {
        val intent = Intent(this, GameplayActivity::class.java)
        intent.putExtra(EXTRA_OTHER_PLAYER, selectedOtherPlayer)
        intent.putExtra(EXTRA_SHIP_PLACEMENT, OpponentParcelable(placementGameBoardView.gameBoard.opponent))
        startActivity(intent)
    }

    /**
     * Save the placement game board when the screen rotates so state is not lost.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_SHIP_PLACEMENT, OpponentParcelable(placementGameBoardView.gameBoard.opponent))
    }
}