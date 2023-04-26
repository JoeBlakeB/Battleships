package com.joeblakeb.battleships.utils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.joeblakeb.battleshipgame.BaseOpponent
import com.joeblakeb.battleshipgame.Battleship
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleshipgame.Opponent
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix

const val EXTRA_SHIP_PLACEMENT: String = "com.joeblakeb.battleships.PlayerShipsPlacement"
const val EXTRA_OTHER_PLAYER: String = "com.joeblakeb.battleships.OtherPlayerType"
const val EXTRA_CURRENT_TURN: String = "com.joeblakeb.battleships.CurrentTurn"
const val EXTRA_GAMEBOARD_ATTACKS: String = "com.joeblakeb.battleships.AttacksGameBoard"
const val EXTRA_GAMEBOARD_SHOOTABLE: String = "com.joeblakeb.battleships.ShootableGameBoard"

const val OTHER_PLAYER_RANDOM: Int = 1
const val OTHER_PLAYER_PROBABILITY: Int = 2

/**
 * A subclass of BaseOpponent for creating and reading parcelables containing opponent data.
 */
data class OpponentParcelable(
    override val ships: List<Battleship>,
    override val columns: Int,
    override val rows: Int
) : Parcelable, BaseOpponent() {
    constructor(opponent: BaseOpponent) : this(opponent.ships, opponent.columns, opponent.rows)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(columns)
        dest.writeInt(rows)
        for (ship in ships) {
            dest.writeIntArray(ship.toIntArray())
        }
    }

    companion object CREATOR : Parcelable.Creator<OpponentParcelable> {
        override fun createFromParcel(parcel: Parcel): OpponentParcelable {
            val columns = parcel.readInt()
            val rows = parcel.readInt()
            val ships = mutableListOf<Battleship>()
            while (parcel.dataAvail() > 0) {
                ships.add(Battleship(parcel.createIntArray()!!))
            }
            return OpponentParcelable(ships, columns, rows)
        }

        override fun newArray(size: Int): Array<OpponentParcelable?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * An object for converting a GameBoard to and from a Parcelable.
 */
class GameBoardParcelable(val gameBoard: GameBoard) : Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        OpponentParcelable(gameBoard.opponent).writeToParcel(parcel, flags)
        parcel.writeByteArray(gameBoard.guessGrid.map { it.toByte() }.toByteArray())
    }

    companion object CREATOR : Parcelable.Creator<GameBoardParcelable> {
        override fun createFromParcel(parcel: Parcel): GameBoardParcelable {
            val opponent = Opponent(parcel.readParcelableCompat<OpponentParcelable>(OpponentParcelable::class.java.classLoader)!!)

            val guessGrid: MutableMatrix<GuessCell> = MutableMatrix(opponent.columns, opponent.rows, GuessCell.UNSET)
            val guessGridBytes = parcel.createByteArray()!!
            for (i in guessGridBytes.indices) {
                guessGrid[i % opponent.columns, i / opponent.columns] = GuessCell.fromByte(guessGridBytes[i])
            }

            return GameBoardParcelable(GameBoard(opponent, guessGrid))
        }

        override fun newArray(size: Int): Array<GameBoardParcelable?> {
            return arrayOfNulls(size)
        }
    }

}

/**
 * Get a parcelable from an Intent, compatible with all android versions.
 *
 * Inline function from stack overflow.
 * @author Niklas (2022, https://stackoverflow.com/a/73311814)
 */
inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

/**
 * Get a parcelable from a Bundle, compatible with all android versions.
 *
 * Inline function from stack overflow.
 * @author Niklas (2022, https://stackoverflow.com/a/73311814)
 */
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

/**
 * Get a parcelable from a Parcel, compatible with all android versions.
 *
 * Based on inline functions by Niklas (2022, https://stackoverflow.com/a/73311814)
 */
inline fun <reified T : Parcelable> Parcel.readParcelableCompat(classLoader: ClassLoader?): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> readParcelable(classLoader, T::class.java)
    else -> @Suppress("DEPRECATION") readParcelable(classLoader) as? T
}