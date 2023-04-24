package com.joeblakeb.battleships.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.joeblakeb.battleshipgame.Battleship
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleshipgame.Opponent
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.GuessResult

/**
 * The game board which allows the player to shoot at the enemy.
 * TODO (Only allows shots when it is the players turn).
 */
class ShootableGameBoardView : BaseGameBoardView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override var gameBoard: GameBoard = GameBoard(Opponent.createRandomPlacement(SHIP_SIZES))

    private val gridChangeListener: BattleshipGrid.BattleshipGridListener =
        BattleshipGrid.BattleshipGridListener { _, _, _ -> invalidate() }

    init {
        gameBoard.addOnGridChangeListener(gridChangeListener)
    }

    override var shipsToDisplay: MutableList<BattleshipOpponent.ShipInfo<Battleship>> =
        mutableListOf()

    private val gestureDetector = GestureDetectorCompat(context, object:
        GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean = true

        /** Shoot at a cell */
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val cell = gridCellAt(e.x, e.y) ?: return true

            if (gameBoard[cell] is GuessCell.UNSET) {
                if (gameBoard.shootAt(cell) is GuessResult.SUNK) {
                    shipsToDisplay.add(gameBoard.opponent.shipAt(cell)!!)
                }
            }

            return true
        }
    })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}