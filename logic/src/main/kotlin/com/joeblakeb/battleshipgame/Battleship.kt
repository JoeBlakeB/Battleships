package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.Ship

class Battleship(
    override val top: Int,
    override val left: Int,
    override val bottom: Int,
    override val right: Int
) : Ship {
    constructor(positions: IntArray) : this(positions[0], positions[1], positions[2], positions[3])

    init {
        require(height >= 1 && width >= 1) { "Ship dimensions are inverted" }
        require(!(height > 1 && width > 1)) { "Ship is too wide" }
    }

    /**
     * Checks if this ship is valid against a list of
     * already validated ships and the grid size.
     *
     * @param columns the width of the grid
     * @param rows the height of the grid
     * @param alreadyValidatedShips The ships already checked
     * @return true if ship are valid
     */
    fun validateAgainstGrid(columns: Int, rows: Int, alreadyValidatedShips: List<Battleship>): Boolean {
        if (left < 0 || right >= columns || top < 0 || bottom >= rows) { return false }
        for (other in alreadyValidatedShips) {
            if ((top in other.rowIndices || bottom in other.rowIndices ||
                other.top in rowIndices || other.bottom in rowIndices
                ) && (
                left in other.columnIndices || right in other.columnIndices ||
                other.left in columnIndices || other.right in columnIndices
            )) { return false }
        }
        return true
    }

    /**
     * @return true if the ship is at those coordinates.
     */
    fun isAtPosition(column: Int, row: Int): Boolean =
        column in left..right && row in top..bottom

    /**
     * Run a function for each cell in the ship until one returns false.
     *
     * @param action the function to run for each cell
     * @return true if the function returned true for all cells
     */
    fun allIndicies(action: (Int, Int) -> Boolean): Boolean {
        for (x in columnIndices) {
            for (y in rowIndices) {
                if (!action(x, y)) { return false }
            }
        }
        return true
    }

    override fun toString(): String =
        "Battleship(top=$top, left=$left, bottom=$bottom, right=$right)"

    /** Creates an int array of the ships positions. */
    fun toIntArray(): IntArray =
        intArrayOf(top, left, bottom, right)

    companion object {
        /**
         * Create a new Battleship from coordinates of ships end, and direction
         * it is facing. Will calculate top, left, bottom, and right.
         *
         * @param size the length of the ship
         * @param column the location of the ships stern
         * @param row the location of the ships stern
         * @param vertical true if the ship is vertical, false if horisontal
         */
        fun createFromSize(size: Int, column: Int, row: Int, vertical: Boolean): Battleship {
            val s = intArrayOf(row, column, row, column)
            s[if (vertical) 2 else 3] += size - 1
            return Battleship(s[0], s[1], s[2], s[3])
        }
    }
}