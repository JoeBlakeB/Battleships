package com.joeblakeb.battleships.views

import android.content.Context
import android.util.AttributeSet
import com.joeblakeb.battleshipgame.GameBoard

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
}