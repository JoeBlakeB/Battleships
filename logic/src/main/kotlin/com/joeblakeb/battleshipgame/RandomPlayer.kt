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
    override fun doShot() {
        val unshotCells = gameBoard.getCoordinatesOfType<GuessCell.UNSET>()

        // TODO(shoot at cells nearby to hits if there are any)

        Timer().schedule(timerTask {
            gameBoard.shootAt(unshotCells.random())
        }, Random.nextLong(400, 800))

    }
}