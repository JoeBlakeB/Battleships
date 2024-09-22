package com.joeblakeb.battleships.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleshipgame.MutableOpponent
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate

/**
 * The view that allows users to chose where their ships are placed,
 * will have randomised ships by default for players who do not want
 * to arrange ships themselves.
 */
class PlacementGameBoardView : BaseGameBoardView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    var mutableOpponent: MutableOpponent = MutableOpponent.createRandomPlacement(BattleshipGrid.DEFAULT_SHIP_SIZES)
        set(value) {
            field = value
            gameBoard = GameBoard(value)
        }

    override var gameBoard: GameBoard = GameBoard(mutableOpponent)

    private var selectedShip: SelectedShip? = null

    private val gestureDetector = GestureDetectorCompat(context, object:
        GestureDetector.SimpleOnGestureListener() {
        /** Set the selected ship for drag and drop */
        override fun onDown(e: MotionEvent): Boolean {
            selectedShip = gridCellAt(e.x, e.y)?.let { coordinate ->
                gameBoard.opponent.shipAt(coordinate.x, coordinate.y)?.let {
                    shipInfo -> SelectedShip(shipInfo.index, coordinate)
                }
            }
            return true
        }

        /** Try to move the selected ship to where the user dragged it to. */
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            if (e1 == null) return false
            val shipToMove = selectedShip ?: return true
            val newCoordinate = gridCellAt(e2.x, e2.y) ?: return true

            if (newCoordinate != shipToMove.previousCoordinate) {
                if (mutableOpponent.tryMoveShip(
                    shipToMove.index,
                    newCoordinate.x - shipToMove.previousCoordinate.x,
                    newCoordinate.y - shipToMove.previousCoordinate.y,
                    newCoordinate.x, newCoordinate.y
                )) {
                    selectedShip?.previousCoordinate = newCoordinate
                    invalidate()
                }
            }

            return true
        }

        /** Tap on a ship to rotate it at the point that was clicked */
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            selectedShip = null

            val gridLocation = gridCellAt(e.x, e.y) ?: return true
            val shipInfo = gameBoard.opponent.shipAt(gridLocation.x, gridLocation.y) ?: return true

            if (mutableOpponent.tryRotateShip(
                    shipInfo.index,
                    gridLocation.x,
                    gridLocation.y
                )) {
                invalidate()
            }

            return true
        }
    })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private data class SelectedShip(
        val index: Int,
        var previousCoordinate: Coordinate
    )
}