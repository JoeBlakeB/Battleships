package com.joeblakeb.battleships.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleshipgame.Opponent
import com.joeblakeb.battleships.R
import com.joeblakeb.battleships.utils.OpponentParcelable
import com.joeblakeb.battleships.utils.PARCELABLE_OPPONENT
import com.joeblakeb.battleships.utils.getParcelableExtraCompat
import com.joeblakeb.battleships.views.AttacksGameBoardView
import com.joeblakeb.battleships.views.ShootableGameBoardView

/**
 * The activity where the player will shoot at the opponent,
 * and see their opponents attacks.
 */
class GameplayActivity : AppCompatActivity() {
    private lateinit var attacksGameBoardView: AttacksGameBoardView
    private lateinit var shootableGameBoardView: ShootableGameBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)

        attacksGameBoardView = findViewById<AttacksGameBoardView>(R.id.attacksGameBoardView)
        shootableGameBoardView = findViewById<ShootableGameBoardView>(R.id.shootableGameBoardView)

        val playerShipsPlacement = intent.getParcelableExtraCompat<OpponentParcelable>(PARCELABLE_OPPONENT)!!

        attacksGameBoardView.gameBoard = GameBoard(Opponent(playerShipsPlacement))
    }
}