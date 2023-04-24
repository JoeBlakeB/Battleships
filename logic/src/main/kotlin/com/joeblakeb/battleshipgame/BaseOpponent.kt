package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate

/**
 * The base code used in all types of opponents.
 */
abstract class BaseOpponent : BattleshipOpponent {
    abstract override val ships: List<Battleship>
    abstract override val columns: Int
    abstract override val rows: Int

    override fun shipAt(column: Int, row: Int): BattleshipOpponent.ShipInfo<Battleship>? {
        val ship = ships.find { it.isAtPosition(column, row) }
        return ship?.let { BattleshipOpponent.ShipInfo(ships.indexOf(ship), it) }
    }

    fun shipAt(coordinate: Coordinate): BattleshipOpponent.ShipInfo<Battleship>? =
        shipAt(coordinate.x, coordinate.y)

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