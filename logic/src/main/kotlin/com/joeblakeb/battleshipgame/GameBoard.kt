package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.GuessResult
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate

/**
 * The class that represents a battleship grid, including the players guesses
 * and the ships they are trying to shoot at.
 */
class GameBoard(
    override val opponent: BaseOpponent
) : BattleshipGrid {
    override val columns: Int
        get() = opponent.columns

    override val rows: Int
        get() = opponent.rows

    val guessGrid: MutableMatrix<GuessCell> = MutableMatrix(columns, rows, GuessCell.UNSET)

    override val shipsSunk: BooleanArray
        get() = opponent.ships.map { guessGrid[it.left, it.top] is GuessCell.SUNK }.toBooleanArray()

    override fun get(column: Int, row: Int): GuessCell {
        require(column >= 0 && row >= 0 && column < columns && row < rows) {
            "Can not get coordinates, they are outside of the grid"
        }
        return guessGrid[column, row]
    }

    /**
     * Get all of the positions that are a certain guess result
     *
     * @return array of cell coordinates
     */
    inline fun <reified T: GuessCell> getCoordinatesOfType(): List<Coordinate> {
        val coordinates = mutableListOf<Coordinate>()
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                if (guessGrid[column, row] is T) {
                    coordinates.add(Coordinate(column, row))
                }
            }
        }
        return coordinates
    }

    /**
     * Shoot at a cell of the grid and change its value in the guess grid.
     * If a ship is sunk, all cells for that ship are updated.
     * Calls event listeners after use.
     *
     * @param column the column shot at
     * @param row the row shot at
     * @return the result of the shot
     */
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
            guessGrid[column, row] = GuessCell.HIT(shipAt.index)

            val sunk = shipAt.ship.allIndicies {
                x, y -> guessGrid[x, y] is GuessCell.HIT
            }

            if (sunk) {
                for (x in shipAt.ship.columnIndices) {
                    for (y in shipAt.ship.rowIndices) {
                        guessGrid[x, y] = GuessCell.SUNK(shipAt.index)
                    }
                }
                result = GuessResult.SUNK(shipAt.index)
            } else {
                result = GuessResult.HIT(shipAt.index)
            }
        }

        notifyGridChangeListeners(column, row)
        return result
    }

    /** A list of all event listeners for this game board */
    private val gridChangeListeners = mutableListOf<BattleshipGrid.BattleshipGridListener>()

    override fun addOnGridChangeListener(listener: BattleshipGrid.BattleshipGridListener) {
        if (listener !in gridChangeListeners) {
            gridChangeListeners.add(listener)
        }
    }

    override fun removeOnGridChangeListener(listener: BattleshipGrid.BattleshipGridListener) {
        gridChangeListeners.remove(listener)
    }

    /** Calls all event listeners for this game board */
    private fun notifyGridChangeListeners(column: Int, row: Int) {
        gridChangeListeners.forEach {
            it.onGridChanged(this, column, row)
        }
    }

    override fun toString(): String = "GameBoard($opponent, GuessGrid(\n" +
        (0 until rows).joinToString("\n") { y ->
            (0 until columns).joinToString(" ", "    ") { x ->
                when(guessGrid[x, y]) {
                    is GuessCell.MISS -> "x"
                    is GuessCell.HIT -> "H"
                    is GuessCell.SUNK -> "S"
                    else -> "."
                }
            }
        } + "\n))"
}