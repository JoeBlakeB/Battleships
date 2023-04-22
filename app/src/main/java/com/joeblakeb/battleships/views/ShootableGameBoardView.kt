package com.joeblakeb.battleships.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import uk.ac.bournemouth.ap.battleshiplib.GuessCell

/**
 * TODO (The game board which allows the player to shoot at the enemy).
 * TODO (Only allows shots when it is the players turn).
 */
class ShootableGameBoardView : BaseGameBoardView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private val gestureDetector = GestureDetectorCompat(context, object:
        GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean = true

        /** Shoot at a cell */
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val cell = gridCellAt(e.x, e.y) ?: return true

            if (gameBoard[cell] is GuessCell.UNSET) {
                gameBoard.shootAt(cell)
                invalidate()
            }

            return true
        }
    })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}