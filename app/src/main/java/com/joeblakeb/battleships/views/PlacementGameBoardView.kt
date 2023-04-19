package com.joeblakeb.battleships.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat

class PlacementGameBoardView : BaseGameBoardView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private var selectedShip: SelectedShip? = null

    private val gestureDetector = GestureDetectorCompat(context, object:
        GestureDetector.SimpleOnGestureListener() {
        /** Set the selected ship for drag and drop */
        override fun onDown(e: MotionEvent): Boolean {
            selectedShip = gridCellAt(e.x, e.y)?.let {
                gameBoard.opponent.shipAt(it.first, it.second)?.let {
                    shipInfo -> SelectedShip(shipInfo.index, it.first, it.second)
                }
            }
            return true
        }

        /** Try to move the selected ship to where the user dragged it to. */
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            val shipToMove = selectedShip ?: return true
            val newLocation = gridCellAt(e2.x, e2.y) ?: return true

            if (newLocation.first != shipToMove.previousColumn || newLocation.second != shipToMove.previousRow) {
                if (gameBoard.opponent.tryMoveShip(
                    shipToMove.index,
                    newLocation.first - shipToMove.previousColumn,
                    newLocation.second - shipToMove.previousRow
                )) {
                    selectedShip?.previousColumn = newLocation.first
                    selectedShip?.previousRow = newLocation.second
                    invalidate()
                }
            }

            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            selectedShip = null
            // TODO tap to rotate ship
            return super.onSingleTapUp(e)
        }
    })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private data class SelectedShip(
        val index: Int,
        var previousColumn: Int,
        var previousRow: Int
    )
}