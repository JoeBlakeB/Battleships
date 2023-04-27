package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate

const val SHOT_DELAY_QUICK: Long = 80
const val SHOT_DELAY_SLOW: Long = 400
const val MAX_SHIP_LENGTH: Int = 5

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
 * Check if a coordinate is directly touching another.
 *
 * @param other the other coordinates to check against
 * @return true if any of the other coordinates is touching
 */
fun Coordinate.isTouching(other: Coordinate): Boolean = (
    (this.x+1 == other.x && this.y == other.y) ||
    (this.x-1 == other.x && this.y == other.y) ||
    (this.x == other.x && this.y+1 == other.y) ||
    (this.x == other.x && this.y-1 == other.y)
)

/** Add two Coordinates together. */
operator fun Coordinate.plus(other: Coordinate) = Coordinate(
    this.x + other.x,
    this.y + other.y
)

/** Multiply a Coordinate by a scalar. */
operator fun Coordinate.times(scalar: Int) = Coordinate(
    this.x * scalar,
    this.y * scalar
)