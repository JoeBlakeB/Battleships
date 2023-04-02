package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_COLUMNS
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_ROWS
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import kotlin.random.Random

class Opponent(
    override val ships: List<Battleship>,
    override val columns: Int = DEFAULT_COLUMNS,
    override val rows: Int = DEFAULT_ROWS,
    val random: Random = Random
) : BattleshipOpponent {
    init {
        checkPlacementValid()
    }

    /**
     * Check that a list of ship placements do not overlap
     * and do not go off the edge off the grid.
     */
    private fun checkPlacementValid() {
        var ship: Battleship
        for (i in ships.indices) {
            ship = ships[i]
            require(ship.left >= 0 && ship.right < columns &&
                    ship.top >= 0 && ship.bottom < rows
            ) { "A ship is off the edge of the game grid" }

            for (j in 0 until i) {
                val other = ships[j]
                require(!((
                    ship.top in other.rowIndices || ship.bottom in other.rowIndices ||
                    other.top in ship.rowIndices || other.bottom in ship.rowIndices
                ) && (
                    ship.left in other.columnIndices || ship.right in other.columnIndices ||
                    other.left in ship.columnIndices || other.right in ship.columnIndices
                ))) { "Some ships overlap" }
            }
        }
    }

    override fun shipAt(column: Int, row: Int): BattleshipOpponent.ShipInfo<Battleship>? {
        val ship = ships.find { it.isAtPosition(column, row) }
        return ship?.let { BattleshipOpponent.ShipInfo(ships.indexOf(ship), it) }
    }


    companion object {
        fun createRandomPlacement(shipSizes: IntArray,columns: Int = DEFAULT_COLUMNS, rows: Int = DEFAULT_ROWS, random: Random = Random): Opponent {
            TODO("Not yet implemented")
        }
    }
}