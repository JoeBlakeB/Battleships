package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.*
import uk.ac.bournemouth.ap.battleshiplib.test.BattleshipTest
import uk.ac.bournemouth.ap.lib.matrix.boolean.BooleanMatrix
import kotlin.random.Random

class StudentBattleshipTest : BattleshipTest<Battleship>() {
    override fun createOpponent(
        columns: Int,
        rows: Int,
        ships: List<Battleship>
    ): Opponent {
        return Opponent(ships, columns, rows)
    }

    override fun transformShip(sourceShip: Ship): Battleship {
        return Battleship(
            sourceShip.top,
            sourceShip.left,
            sourceShip.bottom,
            sourceShip.right
        )
    }

    override fun createOpponent(
        columns: Int,
        rows: Int,
        shipSizes: IntArray,
        random: Random
    ): Opponent {
        // Note that the passing of random allows for repeatable testing
        return Opponent.createRandomPlacement(shipSizes, columns, rows, random)
    }

    override fun createGrid(
        grid: BooleanMatrix,
        opponent: BattleshipOpponent
    ): GameBoard {
        val opponentImplementation =
            opponent as? Opponent
                ?: createOpponent(opponent.columns, opponent.rows, opponent.ships.map { it as? Battleship
                    ?: transformShip(it) })

        return GameBoard(opponentImplementation)
    }
}