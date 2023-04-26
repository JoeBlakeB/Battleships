package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate

const val SHOT_DELAY_QUICK: Long = 80
const val SHOT_DELAY_SLOW: Long = 400

/**
 * The methods that are required for an opponent player to play agaisnt the user,
 * for example: a computer ai player or a remote multiplayer opponent player
 */
interface OtherPlayer {
    val gameBoard: GameBoard

    /** Shoot at the opponent, but does not have to shoot immediately. */
    fun doShot()
}

/**
 * Check if a coordinate is directly touching any of a list of other coordinates.
 *
 * @param others the other coordinates to check against
 * @return true if any of the other coordinates is touching
 */
fun Coordinate.isTouchingAny(others: List<Coordinate>) = others.any {
    (this.x+1 == it.x && this.y == it.y) ||
            (this.x-1 == it.x && this.y == it.y) ||
            (this.x == it.x && this.y+1 == it.y) ||
            (this.x == it.x && this.y-1 == it.y)
}