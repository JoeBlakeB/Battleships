package com.joeblakeb.battleshipgame

import kotlin.random.Random

/**
 * A computer player which guesses a random cell each turn.
 */
class RandomPlayer(
    override val gameBoard: GameBoard
) : OtherPlayer {
    override fun doShot() {
        gameBoard.shootAt(Random.nextInt(10), Random.nextInt(10))
        // TODO(only shoot at un guessed cells)
    }
}