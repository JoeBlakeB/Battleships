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
    override fun shipAt(column: Int, row: Int): BattleshipOpponent.ShipInfo<Battleship>? {
        TODO("Not yet implemented")
    }


    companion object {
        fun createRandomPlacement(shipSizes: IntArray,columns: Int = DEFAULT_COLUMNS, rows: Int = DEFAULT_ROWS, random: Random = Random): Opponent {
            TODO("Not yet implemented")
        }
    }
}