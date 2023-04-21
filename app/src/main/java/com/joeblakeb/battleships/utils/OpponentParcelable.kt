package com.joeblakeb.battleships.utils

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