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
            sourceShip.right,
            sourceShip.bottom,
            sourceShip.left
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
        val opponentImplementation = createOpponent(
            opponent.columns, opponent.rows, opponent.ships.map { transformShip(it) })

        return GameBoard(opponentImplementation)
    }
}