package org.example.student.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.*
import uk.ac.bournemouth.ap.battleshiplib.test.BattleshipTest
import uk.ac.bournemouth.ap.lib.matrix.boolean.BooleanMatrix
import kotlin.random.Random

class StudentBattleshipTest : BattleshipTest<StudentShip>() {
    override fun createOpponent(
        columns: Int,
        rows: Int,
        ships: List<StudentShip>
    ): StudentBattleshipOpponent {
        return TODO("Create an instance of StudentBattleshipOpponent with the dimensions and ships")
    }

    override fun transformShip(sourceShip: Ship): StudentShip {
        return TODO("create an instance of StudentShip that maps the given ship data")
    }

    override fun createOpponent(
        columns: Int,
        rows: Int,
        shipSizes: IntArray,
        random: Random
    ): StudentBattleshipOpponent {
        // Note that the passing of random allows for repeatable testing
        return TODO("Create an instance of StudentBattleshipOpponent for the given game size, " +
                "target ship sizes and random generator")
    }

    override fun createGrid(
        grid: BooleanMatrix,
        opponent: BattleshipOpponent
    ): StudentBattleshipGrid {
        // If the opponent is not a StudentBattleshipOpponent, create it based upon the passed in data
        val studentOpponent =
            opponent as? StudentBattleshipOpponent
                ?: createOpponent(opponent.columns, opponent.rows, opponent.ships.map { it as? StudentShip ?: transformShip(it) })

        return StudentBattleshipGrid(studentOpponent)
    }
}

