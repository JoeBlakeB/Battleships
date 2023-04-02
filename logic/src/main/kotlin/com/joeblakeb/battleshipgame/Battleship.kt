package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.Ship

class Battleship(
    override val top: Int,
    override val left: Int,
    override val bottom: Int,
    override val right: Int
) : Ship {
    init {
        require(height >= 1 && width >= 1) { "Ship dimensions are inverted" }
        require((height == 1).xor(width == 1)) { "Ship dimensions are inverted" }
    }

    /**
     * @return Boolean for if the ship is at those coordinates.
     */
    fun isAtPosition(column: Int, row: Int): Boolean =
        column in left..right && row in top..bottom

    override fun toString(): String {
        return "Battleship(top=$top, left=$left, bottom=$bottom, right=$right)"
    }
}