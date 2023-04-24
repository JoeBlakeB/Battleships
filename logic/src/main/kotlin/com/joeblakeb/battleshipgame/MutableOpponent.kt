package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import kotlin.random.Random

/**
 * A mutable opponents ships, used for chosing where ships are placed,
 * ship positions are only checked when they are moved in the grid.
 */
class MutableOpponent private constructor(
    override val ships: MutableList<Battleship>,
    override val columns: Int = BattleshipGrid.DEFAULT_COLUMNS,
    override val rows: Int = BattleshipGrid.DEFAULT_ROWS
) : BaseOpponent() {
    constructor(opponent: BaseOpponent) : this(opponent.ships.toMutableList(), opponent.columns, opponent.rows)

    /**
     * Try to move a ship to a new position and check if it can go there.
     *
     * @param shipIndex the index of the ship to move
     * @param columnsToMove the number of columns to move the ship by
     * @param rowsToMove the number of rows to move the ship by
     * @param centerColumn the column that is the movements midpoint
     * @param centerRow the row that is the movements midpoint
     * @return true if the move was successful, or false if the ship cannot go there
     */
    fun tryMoveShip(shipIndex: Int, columnsToMove: Int, rowsToMove: Int, centerColumn: Int, centerRow: Int): Boolean {
        return tryReplaceShip(shipIndex,
            ships[shipIndex].top + rowsToMove,
            ships[shipIndex].left + columnsToMove,
            ships[shipIndex].bottom + rowsToMove,
            ships[shipIndex].right + columnsToMove,
            centerColumn, centerRow
        )
    }

    /**
     * Try to rotate a ship and check if it can go there.
     *
     * @param shipIndex the index of the ship to move
     * @param centerColumn the column that is the rotations midpoint
     * @param centerRow the row that is the rotations midpoint
     * @return true if the move was successful, or false if the ship cannot go there
     */
    fun tryRotateShip(shipIndex: Int, centerColumn: Int, centerRow: Int): Boolean {
        val ship = ships[shipIndex]
        val topOffset = centerRow - ship.top
        val leftOffset = centerColumn - ship.left

        val top = ship.top + topOffset - leftOffset
        val left = ship.left + leftOffset - topOffset
        val bottom = top + ship.width - 1
        val right = left + ship.height - 1

        return tryReplaceShip(shipIndex,
            top, left, bottom, right,
            centerColumn, centerRow
        )
    }

    /**
     * Try to replace a Battleship with a new one if it is valid,
     * also checks alternate positions for the ship to go which also
     * include the center point (the users selected new position).
     *
     * @param shipIndex the index of the ship
     * @param top of the initial ship position
     * @param left of the initial ship position
     * @param bottom of the initial ship position
     * @param right of the initial ship position
     * @param centerColumn the column that is the movements midpoint
     * @param centerRow the row that is the movements midpoint
     * @return true if the ship was successfuly placed
     */
    private fun tryReplaceShip(shipIndex: Int,
                               top: Int, left: Int, bottom: Int, right: Int,
                               centerColumn: Int, centerRow: Int): Boolean {
        val otherShips = ships.filterIndexed{ index, _ -> index != shipIndex }
        for (columnChange in (0 .. centerColumn - left) + (-1 downTo centerColumn - right)) {
            for (rowChange in (0 .. centerRow - top) + (-1 downTo centerRow - bottom)) {
                val newShip = Battleship(
                    top + rowChange,
                    left + columnChange,
                    bottom + rowChange,
                    right + columnChange
                )
                if (newShip.validateAgainstGrid(columns, rows, otherShips)) {
                    ships[shipIndex] = newShip
                    return true
                }
            }
        }
        return false
    }

    companion object {
        /**
         * Places ships of input array length in random valid pisitions.
         *
         * @param shipSizes an array of ship lengths
         * @param columns the width of the grid
         * @param rows the height of the grid
         * @param random for repeatability in testing
         */
        fun createRandomPlacement(
            shipSizes: IntArray,
            columns: Int = BattleshipGrid.DEFAULT_COLUMNS,
            rows: Int = BattleshipGrid.DEFAULT_ROWS,
            random: Random = Random.Default
        ): MutableOpponent = MutableOpponent(
            Opponent.createRandomPlacement(
                shipSizes, columns, rows, random).ships.toMutableList(),
            columns, rows)
    }
}