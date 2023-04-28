package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent

/**
 * The base code used in all types of opponents.
 */
abstract class BaseOpponent : BattleshipOpponent {
    abstract override val ships: List<Battleship>
    abstract override val columns: Int
    abstract override val rows: Int

    /**
     * Determine the ship at a given position at a given position.
     *
     * @param column of the cell to check
     * @param row of the cell to check
     * @return ShipInfo with the ship and its index
     */
    override fun shipAt(column: Int, row: Int): BattleshipOpponent.ShipInfo<Battleship>? {
        val ship = ships.find { it.isAtPosition(column, row) }
        return ship?.let { BattleshipOpponent.ShipInfo(ships.indexOf(ship), it) }
    }

    /**
     * Returns the string representation of the ships on a grid.
     *
     * from BattleshipOpponent.toMatrixString in testlib.BattleshipTest
     * @author Paul De Vrieze
     */
    override fun toString(): String = "Opponent(\n" +
        (0 until rows).joinToString("\n") { y ->
            (0 until columns).joinToString(" ", "    ") { x ->
                shipAt(x, y)?.index?.toString() ?: "."
            }
        } + "\n)"
}