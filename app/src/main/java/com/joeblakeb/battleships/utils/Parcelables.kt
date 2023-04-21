package com.joeblakeb.battleships.utils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.joeblakeb.battleshipgame.Battleship
import com.joeblakeb.battleshipgame.Opponent

const val PARCELABLE_OPPONENT: String = "com.joeblakeb.battleships.PlayerShipsPlacement"

data class OpponentParcelable(val opponent: Opponent) : Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        for (ship in opponent.ships) {
            dest.writeIntArray(ship.toIntArray())
        }
    }

    companion object CREATOR : Parcelable.Creator<OpponentParcelable> {
        override fun createFromParcel(parcel: Parcel): OpponentParcelable {
            val ships = mutableListOf<Battleship>()
            while (parcel.dataAvail() > 0) {
                ships.add(Battleship(parcel.createIntArray()!!))
            }
            return OpponentParcelable(Opponent(ships))
        }

        override fun newArray(size: Int): Array<OpponentParcelable?> {
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