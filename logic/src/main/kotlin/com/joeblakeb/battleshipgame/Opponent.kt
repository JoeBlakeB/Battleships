package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_COLUMNS
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_ROWS
import kotlin.random.Random

/**
 * An opponent used in the gameplay, ships cannot be moved after creation,
 * so checks that they are in the correct place on initialization.
 */
class Opponent(
    override val ships: List<Battleship>,
    override val columns: Int = DEFAULT_COLUMNS,
    override val rows: Int = DEFAULT_ROWS
) : BaseOpponent() {
    constructor(opponent: BaseOpponent) : this(opponent.ships, opponent.columns, opponent.rows)

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
            require(shipSizes.max() <= maxOf(columns, rows)) { "Grid too small" }

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