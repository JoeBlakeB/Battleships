package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.GuessResult

class GameBoard(
    override val opponent: BattleshipOpponent
) : BattleshipGrid {
    override val columns: Int
        get() = opponent.columns

    override val rows: Int
        get() = opponent.rows

    override val shipsSunk: BooleanArray
        get() = TODO("Not yet implemented")

    override fun get(column: Int, row: Int): GuessCell {
        TODO("Not yet implemented")
    }

    override fun shootAt(column: Int, row: Int): GuessResult {
        TODO("Not yet implemented")
    }

    override fun addOnGridChangeListener(listener: BattleshipGrid.BattleshipGridListener) {
        TODO("Not yet implemented")
    }

    override fun removeOnGridChangeListener(listener: BattleshipGrid.BattleshipGridListener) {
        TODO("Not yet implemented")
    }
}