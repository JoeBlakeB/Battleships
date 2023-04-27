package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import java.util.Timer
import kotlin.concurrent.timerTask
import kotlin.random.Random

/**
 * A computer player which guesses a random cell each turn.
 */
class RandomPlayer(
    override val gameBoard: GameBoard
) : OtherPlayer {

    /**
     * Shoot at a random cell, if there is a hit then shoot near to that cell.
     */
    override fun doShot() {
        var unshotCells = gameBoard.getCoordinatesOfType<GuessCell.UNSET>()
        val hitCells = gameBoard.getCoordinatesOfType<GuessCell.HIT>()
        var shotDelay = SHOT_DELAY_SLOW

        if (hitCells.isNotEmpty()) {
            unshotCells = unshotCells.filter { hitCells.any { other -> other.isTouching(it) } }
            shotDelay = SHOT_DELAY_QUICK
        }

        val cellToShoot = unshotCells.random()

        Timer().schedule(timerTask {
            gameBoard.shootAt(cellToShoot)
        }, Random.nextLong(shotDelay, shotDelay * 2))
    }
}