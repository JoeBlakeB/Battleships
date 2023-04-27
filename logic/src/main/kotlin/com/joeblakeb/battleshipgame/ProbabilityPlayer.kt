package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate
import java.util.Timer
import kotlin.concurrent.timerTask
import kotlin.random.Random

/**
 * A computer player which guesses where to shoot based on which cells are most likely to
 * contain a ship. Works by calculating a score by looking at nearby cells and then choses
 * one of the highest scoring cells.
 */
class ProbabilityPlayer(
    override val gameBoard: GameBoard
) : OtherPlayer {
    /** The four directions that a ship could be */
    private val directions = listOf(
        Coordinate(-1, 0),
        Coordinate(1, 0),
        Coordinate(0, 1),
        Coordinate(0, -1)
    )

    /**
     * Check if there are any already hit ships, and call the correct
     * function for shooting at the grid.
     */
    override fun doShot() = Timer().schedule(
        if (gameBoard.guessGrid.any { it is GuessCell.HIT }) {
            timerTask { shootAtFound() }
        } else {
            timerTask { shootAtAny() }
        }, Random.nextLong(SHOT_DELAY_QUICK, SHOT_DELAY_SLOW))

    /**
     * TODO
     * Shoot at one of the cells with the highest chance of containing a ship,
     * randomly choses one of the most likely to avoid haivng a consistent pattern.
     */
    private fun shootAtAny() {
        val unshotCells = gameBoard.getCoordinatesOfType<GuessCell.UNSET>()
        gameBoard.shootAt(unshotCells.random())
    }

    /**
     * Shoot at a cell where an already found ship is likely to be, if there are two
     * hit cells in a row, try to hit a third position in the same row.
     */
    private fun shootAtFound() {
        val unshotCells = gameBoard.getCoordinatesOfType<GuessCell.UNSET>()
        val hitCells = gameBoard.getCoordinatesOfType<GuessCell.HIT>()

        // Get list of potential next shots
        val nextShots = mutableListOf<NextShotInfo>()
        for (hitCell in hitCells) {
            for (direction in directions) {
                if (hitCell + direction in unshotCells) {
                    nextShots.add(NextShotInfo(hitCell, direction))
                }
            }
        }

        // Add score based on how many hit cells are in the other direction
        for (nextShot in nextShots) {
            for (i in 0 downTo 0 - MAX_SHIP_LENGTH) {
                if (gameBoard.getOrNull(nextShot.cellAtDistance(i)) is GuessCell.HIT) {
                    nextShot.score += 10
                } else {
                    break
                }
            }
        }

        // Do one of the highest score shots at random
        val maxScore = nextShots.maxByOrNull { it.score }?.score
        gameBoard.shootAt(nextShots.filter { it.score == maxScore }.random().cellToShoot)
    }

    /** A potential next shot after a ship has already been hit, and its ranking. */
    private data class NextShotInfo (
        val hitCell: Coordinate,
        /** The direction to the unshot cell. */
        val direction: Coordinate,
        /** A higher score means a shot is more likely to be a hit. */
        var score: Int = 0
    ) {
        /** Get the cell distance away from the hitCell in the correct direction. */
        fun cellAtDistance(distance: Int): Coordinate = hitCell + (direction * distance)

        /** The cell that the nextShot is suppoed to be shooting at. */
        val cellToShoot: Coordinate = hitCell + direction
    }
}