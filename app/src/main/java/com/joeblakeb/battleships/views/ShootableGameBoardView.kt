package com.joeblakeb.battleships.views

import android.content.Context
import android.util.AttributeSet

/**
 * TODO (The game board which allows the player to shoot at the enemy).
 * TODO (Only allows shots when it is the players turn).
 */
class ShootableGameBoardView : BaseGameBoardView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)
}