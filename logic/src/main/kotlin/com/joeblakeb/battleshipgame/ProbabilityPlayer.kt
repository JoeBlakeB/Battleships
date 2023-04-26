package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.GuessCell

class ProbabilityPlayer(
    override val gameBoard: GameBoard
) : OtherPlayer {
    override fun doShot() {
        val unshotCells = gameBoard.getCoordinatesOfType<GuessCell.UNSET>()
        gameBoard.shootAt(unshotCells[0])
        // TODO("Not yet implemented: for implementation of difficulty selector only")
    }
}