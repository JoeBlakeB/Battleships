package com.joeblakeb.battleships.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleshipgame.Opponent
import com.joeblakeb.battleshipgame.OtherPlayer
import com.joeblakeb.battleshipgame.ProbabilityPlayer
import com.joeblakeb.battleshipgame.RandomPlayer
import com.joeblakeb.battleships.R
import com.joeblakeb.battleships.utils.EXTRA_SHIP_PLACEMENT
import com.joeblakeb.battleships.utils.EXTRA_OTHER_PLAYER
import com.joeblakeb.battleships.utils.OTHER_PLAYER_PROBABILITY
import com.joeblakeb.battleships.utils.OTHER_PLAYER_RANDOM
import com.joeblakeb.battleships.utils.OpponentParcelable
import com.joeblakeb.battleships.utils.getParcelableExtraCompat
import com.joeblakeb.battleships.views.AttacksGameBoardView
import com.joeblakeb.battleships.views.GameplayGameBoardView
import com.joeblakeb.battleships.views.SHIP_SIZES
import com.joeblakeb.battleships.views.ShootableGameBoardView
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import kotlin.random.Random

/**
 * The activity where the player will shoot at the opponent,
 * and see their opponents attacks.
 */
class GameplayActivity : AppCompatActivity() {
    private lateinit var attacksGameBoardView: AttacksGameBoardView
    private lateinit var shootableGameBoardView: ShootableGameBoardView
    private lateinit var gameBoardViews: List<GameplayGameBoardView>

    private lateinit var turnStatus: TextView
    private lateinit var turnStrings: List<String>

    private var selectedOtherPlayer: Int = 1

    private var turn: Int = Random.nextInt(2)

    private val gridChangeListener: BattleshipGrid.BattleshipGridListener =
        BattleshipGrid.BattleshipGridListener { _, _, _ -> runOnUiThread { doNextTurn() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)

        attacksGameBoardView = findViewById(R.id.attacksGameBoardView)
        shootableGameBoardView = findViewById(R.id.shootableGameBoardView)
        turnStatus = findViewById(R.id.textViewTurn)

        selectedOtherPlayer = intent.getIntExtra(EXTRA_OTHER_PLAYER, OTHER_PLAYER_RANDOM)

        val playerShipsPlacement = intent.getParcelableExtraCompat<OpponentParcelable>(EXTRA_SHIP_PLACEMENT)!!
        val otherPlayerGameBoard = GameBoard(Opponent(playerShipsPlacement))
        attacksGameBoardView.setOtherPlayer(createOtherPlayer(selectedOtherPlayer, otherPlayerGameBoard))

        val opponentShipsPlacement = Opponent.createRandomPlacement(SHIP_SIZES)
        shootableGameBoardView.setGameBoard(GameBoard(opponentShipsPlacement))

        attacksGameBoardView.gameBoard.addOnGridChangeListener(gridChangeListener)
        shootableGameBoardView.gameBoard.addOnGridChangeListener(gridChangeListener)

        gameBoardViews = listOf(
            shootableGameBoardView,
            attacksGameBoardView
        )

        turnStrings = listOf(
            getString(R.string.turn_you),
            getString(R.string.turn_enemy)
        )

        doNextTurn()
    }

    /**
     * Tell the next player to have their turn, or if the game is over tell the user.
     */
    private fun doNextTurn() {
        if (attacksGameBoardView.gameBoard.isFinished or shootableGameBoardView.gameBoard.isFinished) {
            turnStatus.text = ""
            val snackbar = Snackbar.make(
                gameBoardViews[turn] as View,
                listOf(
                    R.string.win_you,
                    R.string.win_enemy
                )[turn],
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar.setAction(R.string.play_again_button) {
                finish()
            }
            snackbar.show()
        } else {
            turn = (turn + 1) % 2
            turnStatus.text = turnStrings[turn]
            gameBoardViews[turn].haveTurn()
        }
    }

    /**
     * Get an OtherPlayer object from its Int selector.
     *
     * @param selectedOtherPlayer OTHER_PLAYER_*
     * @param otherPlayerGameBoard The game board to give to the OtherPlayer
     * @return An OtherPlayer object for the user to play against
     */
    private fun createOtherPlayer(selectedOtherPlayer: Int, otherPlayerGameBoard: GameBoard): OtherPlayer {
        return when(selectedOtherPlayer) {
            OTHER_PLAYER_PROBABILITY -> ProbabilityPlayer(otherPlayerGameBoard)
            else -> RandomPlayer(otherPlayerGameBoard)
        }
    }
}