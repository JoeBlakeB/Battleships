package com.joeblakeb.battleships.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleshipgame.OtherPlayer
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid

/**
 * TODO (The game board view which shows the attacks of an enemy
 * against the players ships).
 */
class AttacksGameBoardView : BaseGameBoardView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override lateinit var gameBoard: GameBoard
        private set

    private lateinit var player: OtherPlayer

    private val gridChangeListener: BattleshipGrid.BattleshipGridListener =
        BattleshipGrid.BattleshipGridListener { _, _, _ -> invalidate() }

    /** Set the other player logic and game board, then add the event listener to it */
    fun setGameBoard(newOtherPlayer: OtherPlayer) {
        player = newOtherPlayer
        gameBoard = newOtherPlayer.gameBoard
        gameBoard.addOnGridChangeListener(gridChangeListener)
    }






    /** Temporary stuff */
    private val gestureDetector = GestureDetectorCompat(context, object:
        GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean = true
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            player.doShot()
            return true
        }})
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}