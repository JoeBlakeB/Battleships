package com.joeblakeb.battleshipgame

/**
 * The methods that are required for an opponent player to play agaisnt the user,
 * for example: a computer ai player or a remote multiplayer opponent player
 */
interface OtherPlayer {
    val gameBoard: GameBoard

    fun doShot()
}