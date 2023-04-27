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
import com.joeblakeb.battleships.utils.EXTRA_CURRENT_TURN
import com.joeblakeb.battleships.utils.EXTRA_GAMEBOARD_ATTACKS
import com.joeblakeb.battleships.utils.EXTRA_GAMEBOARD_SHOOTABLE
import com.joeblakeb.battleships.utils.EXTRA_SHIP_PLACEMENT
import com.joeblakeb.battleships.utils.EXTRA_OTHER_PLAYER
import com.joeblakeb.battleships.utils.OTHER_PLAYER_PROBABILITY
import com.joeblakeb.battleships.utils.OTHER_PLAYER_RANDOM
import com.joeblakeb.battleships.utils.GameBoardParcelable
import com.joeblakeb.battleships.utils.OpponentParcelable
import com.joeblakeb.battleships.utils.getParcelableCompat
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

    private lateinit var textViewEnemy: TextView
    private lateinit var turnStatus: TextView
    private lateinit var turnStrings: List<String>

    private var selectedOtherPlayer: Int = 1

    /**
     * Either 1 or 2 for each player, or greater than 8 after screen rotate
     * to stop bug causing computer to take multiple turns.
     */
    private var turn: Int = Random.nextInt(2)

    private val gridChangeListener: BattleshipGrid.BattleshipGridListener =
        BattleshipGrid.BattleshipGridListener { _, _, _ -> runOnUiThread { doNextTurn() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)

        attacksGameBoardView = findViewById(R.id.attacksGameBoardView)
        shootableGameBoardView = findViewById(R.id.shootableGameBoardView)
        textViewEnemy = findViewById(R.id.textViewEnemy)
        turnStatus = findViewById(R.id.textViewTurn)

        selectedOtherPlayer = intent.getIntExtra(EXTRA_OTHER_PLAYER, OTHER_PLAYER_RANDOM)

        if (savedInstanceState != null) {
            turn = savedInstanceState.getInt(EXTRA_CURRENT_TURN)
            attacksGameBoardView.setOtherPlayer(createOtherPlayer(selectedOtherPlayer,
                savedInstanceState.getParcelableCompat<GameBoardParcelable>(EXTRA_GAMEBOARD_ATTACKS)!!.gameBoard))
            shootableGameBoardView.setGameBoard(
                savedInstanceState.getParcelableCompat<GameBoardParcelable>(EXTRA_GAMEBOARD_SHOOTABLE)!!.gameBoard)
        } else {
            val playerShipsPlacement = intent.getParcelableExtraCompat<OpponentParcelable>(EXTRA_SHIP_PLACEMENT)!!
            val otherPlayerGameBoard = GameBoard(Opponent(playerShipsPlacement))
            attacksGameBoardView.setOtherPlayer(createOtherPlayer(selectedOtherPlayer, otherPlayerGameBoard))

            val opponentShipsPlacement = Opponent.createRandomPlacement(SHIP_SIZES)
            shootableGameBoardView.setGameBoard(GameBoard(opponentShipsPlacement))
        }

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

        if (turn <= 8) {
            doNextTurn()
        } else {
            // Screen was rotated and it is the computers turn,
            // wait so they dont get multiple turns.
            turnStatus.text = turnStrings[1]
        }
    }

    /**
     * Tell the next player to have their turn, or if the game is over tell the user.
     */
    private fun doNextTurn() {
        if (turn == 8) {
            turn -= 9
        }
        if (attacksGameBoardView.gameBoard.isFinished or shootableGameBoardView.gameBoard.isFinished) {
            turn %= 2
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
     * Save the placement game board when the screen rotates so state is not lost.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_CURRENT_TURN,
            if (attacksGameBoardView.gameBoard.isFinished or shootableGameBoardView.gameBoard.isFinished)
                turn else (turn % 8) + 8)
        attacksGameBoardView.gameBoard.removeOnGridChangeListener(gridChangeListener)
        shootableGameBoardView.gameBoard.removeOnGridChangeListener(gridChangeListener)
        outState.putParcelable(EXTRA_GAMEBOARD_ATTACKS, GameBoardParcelable(attacksGameBoardView.gameBoard))
        outState.putParcelable(EXTRA_GAMEBOARD_SHOOTABLE, GameBoardParcelable(shootableGameBoardView.gameBoard))
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
            OTHER_PLAYER_PROBABILITY -> {
                textViewEnemy.text = getString(R.string.name_enemy_hard)
                ProbabilityPlayer(otherPlayerGameBoard)
            }

            else -> {
                textViewEnemy.text = getString(R.string.name_enemy_easy)
                RandomPlayer(otherPlayerGameBoard)
            }
        }
    }
}