package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_COLUMNS
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_ROWS
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import kotlin.random.Random

class Opponent(
    override val ships: MutableList<Battleship>,
    override val columns: Int = DEFAULT_COLUMNS,
    override val rows: Int = DEFAULT_ROWS
) : BattleshipOpponent {
    init {
        checkPlacementValid()
    }

    /**
     * Check that a list of ship placements do not overlap
     * and do not go off the edge off the grid.
     */
    private fun checkPlacementValid() {
        val validShips = mutableListOf<Battleship>()
        for (ship in ships) {
            require(ship.validateAgainstGrid(columns, rows, validShips)) {
                "The placement of ships is invalid"
            }
            validShips.add(ship)
        }
    }

    /**
     * Try to move a ship to a new position and check if it can go there.
     *
     * @param shipIndex the index of the ship to move
     * @param columnsToMove the number of columns to move the ship by
     * @param rowsToMove the number of rows to move the ship by
     * @return true if the move was successful, or false if the ship cannot go there
     */
    fun tryMoveShip(shipIndex: Int, columnsToMove: Int, rowsToMove: Int): Boolean {
        val newShip = Battleship(
            ships[shipIndex].top + rowsToMove,
            ships[shipIndex].left + columnsToMove,
            ships[shipIndex].bottom + rowsToMove,
            ships[shipIndex].right + columnsToMove
        )

        val valid = newShip.validateAgainstGrid(columns, rows,
            ships.filterIndexed{ index, _ -> index != shipIndex })

        if (valid) {
            ships[shipIndex] = newShip
        }

        return valid
    }

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
            columns: Int = DEFAULT_COLUMNS,
            rows: Int = DEFAULT_ROWS,
            random: Random = Random.Default
        ): Opponent {
            require(shipSizes.max() <= maxOf(columns, rows)) {"Grid too small"}

            val sizesToPlace = shipSizes.toMutableList()
            val placedShips = mutableListOf<Battleship>()
            while (sizesToPlace.isNotEmpty()) {
                val size = sizesToPlace[0]
                val vertical = random.nextBoolean()
                val column = random.nextInt(columns)
                val row = random.nextInt(rows)

                val newShip = Battleship.createFromSize(size, column, row, vertical)

                if (newShip.validateAgainstGrid(columns, rows, placedShips)) {
                    sizesToPlace.removeAt(0)
                    placedShips.add(newShip)
                }
            }

            return Opponent(placedShips, columns, rows)
        }
    }
}