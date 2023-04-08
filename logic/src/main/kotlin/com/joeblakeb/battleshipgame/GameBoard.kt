package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.GuessResult
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix

class GameBoard(
    override var opponent: Opponent
) : BattleshipGrid {
    override val columns: Int
        get() = opponent.columns

    override val rows: Int
        get() = opponent.rows

    private val guessGrid: MutableMatrix<GuessCell> = MutableMatrix(columns, rows, GuessCell.UNSET)

    override val shipsSunk: BooleanArray
        get() = opponent.ships.map { it.sunk }.toBooleanArray()

    override fun get(column: Int, row: Int): GuessCell {
        require(column >= 0 && row >= 0 && column < columns && row < rows) {
            "Can not get coordinates, they are outside of the grid"
        }
        return guessGrid[column, row]
    }

    override fun shootAt(column: Int, row: Int): GuessResult {
        require(column >= 0 && row >= 0 && column < columns && row < rows) {
            "Can not shoot at coordinates, they are outside of the grid"
        }
        require(guessGrid[column, row] == GuessCell.UNSET) {
            "Cell has already been shot at"
        }
        val shipAt = opponent.shipAt(column, row)
        val result: GuessResult

        if (shipAt == null) {
            guessGrid[column, row] = GuessCell.MISS
            result = GuessResult.MISS
        } else {
            val sunk = shipAt.ship.shootAt(column, row)
            if (sunk) {
                for (x in shipAt.ship.columnIndices) {
                    for (y in shipAt.ship.rowIndices) {
                        guessGrid[x, y] = GuessCell.SUNK(shipAt.index)
                    }
                }
                result = GuessResult.SUNK(shipAt.index)
            } else {
                guessGrid[column, row] = GuessCell.HIT(shipAt.index)
                result = GuessResult.HIT(shipAt.index)
            }
        }

        notifyGridChangeListeners(column, row)
        return result
    }

    private val gridChangeListeners = mutableListOf<BattleshipGrid.BattleshipGridListener>()

    override fun addOnGridChangeListener(listener: BattleshipGrid.BattleshipGridListener) {
        if (listener !in gridChangeListeners) {
            gridChangeListeners.add(listener)
        }
    }

    override fun removeOnGridChangeListener(listener: BattleshipGrid.BattleshipGridListener) {
        gridChangeListeners.remove(listener)
    }

    private fun notifyGridChangeListeners(column: Int, row: Int) {
        gridChangeListeners.forEach {
            it.onGridChanged(this, column, row)
        }
    }

    override fun toString(): String = "GameBoard($opponent)"
}