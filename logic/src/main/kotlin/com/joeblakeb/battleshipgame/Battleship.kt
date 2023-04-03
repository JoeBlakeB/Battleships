package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.Ship

class Battleship(
    override val top: Int,
    override val right: Int,
    override val bottom: Int,
    override val left: Int
) : Ship {
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

    override fun toString(): String {
        return "Battleship(top=$top, left=$left, bottom=$bottom, right=$right)"
    }

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
            s[if (vertical) 2 else 1] += size - 1
            return Battleship(s[0], s[1], s[2], s[3])
        }
    }
}