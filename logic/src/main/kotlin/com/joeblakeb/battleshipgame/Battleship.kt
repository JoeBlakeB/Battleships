package com.joeblakeb.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.Ship

class Battleship(
    override val top: Int,
    override val left: Int,
    override val bottom: Int,
    override val right: Int
) : Ship {
}