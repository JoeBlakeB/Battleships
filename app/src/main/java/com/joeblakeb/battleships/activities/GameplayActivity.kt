package com.joeblakeb.battleships.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joeblakeb.battleships.R
import com.joeblakeb.battleships.utils.OpponentParcelable
import com.joeblakeb.battleships.utils.PARCELABLE_OPPONENT
import com.joeblakeb.battleships.views.AttacksGameBoardView
import com.joeblakeb.battleships.views.ShootableGameBoardView

class GameplayActivity : AppCompatActivity() {
    private lateinit var attacksGameBoardView: AttacksGameBoardView
    private lateinit var shootableGameBoardView: ShootableGameBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)

        attacksGameBoardView = findViewById<AttacksGameBoardView>(R.id.attacksGameBoardView)
        shootableGameBoardView = findViewById<ShootableGameBoardView>(R.id.shootableGameBoardView)

        val playerShipsPlacement = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PARCELABLE_OPPONENT, OpponentParcelable::class.java)!!.opponent
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<OpponentParcelable>(PARCELABLE_OPPONENT)!!.opponent
        }

        attacksGameBoardView.gameBoard.opponent = playerShipsPlacement
    }
}