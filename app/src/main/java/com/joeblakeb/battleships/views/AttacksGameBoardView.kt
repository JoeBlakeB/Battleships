package com.joeblakeb.battleships.views

import android.content.Context
import android.util.AttributeSet
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleshipgame.OtherPlayer
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid

/**
 * TODO (The game board view which shows the attacks of an enemy
 * against the players ships).
 */
class AttacksGameBoardView : BaseGameBoardView, GameplayGameBoardView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    /** Tell the player logic to have a turn */
    override fun haveTurn() {
        player.doShot()
    }

    override lateinit var gameBoard: GameBoard
        private set

    private lateinit var player: OtherPlayer

    private val gridChangeListener: BattleshipGrid.BattleshipGridListener =
        BattleshipGrid.BattleshipGridListener { _, _, _ -> invalidate() }

    /** Set the other player logic and game board, then add the event listener to it */
    fun setOtherPlayer(newOtherPlayer: OtherPlayer) {
        player = newOtherPlayer
        gameBoard = newOtherPlayer.gameBoard
        gameBoard.addOnGridChangeListener(gridChangeListener)
    }
}