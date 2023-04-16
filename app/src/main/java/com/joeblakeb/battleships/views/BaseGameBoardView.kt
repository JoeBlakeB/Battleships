package com.joeblakeb.battleships.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.joeblakeb.battleshipgame.Battleship
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleshipgame.Opponent
import com.joeblakeb.battleships.R

val SHIP_SIZES: IntArray = intArrayOf(5,4,3,3,2)

open class BaseGameBoardView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private var gameBoard = GameBoard(Opponent.createRandomPlacement(SHIP_SIZES))

    private val shipsToDisplay: List<Pair<Int, Battleship>>
        get() = gameBoard.opponent.ships.mapIndexed {
                index, ship -> Pair(index, ship)
        }

    private var drawableShips: Array<VectorDrawableCompat> = arrayOf(
        VectorDrawableCompat.create(resources, R.drawable.ship_carrier, null)!!,    // 5
        VectorDrawableCompat.create(resources, R.drawable.ship_battleship, null)!!, // 4
        VectorDrawableCompat.create(resources, R.drawable.ship_submarine, null)!!,  // 3
        VectorDrawableCompat.create(resources, R.drawable.ship_cruiser, null)!!,    // 3
        VectorDrawableCompat.create(resources, R.drawable.ship_destroyer, null)!!,  // 2
    )

    private val columns: Int get() = gameBoard.columns
    private val rows: Int get() = gameBoard.rows

    private var gridLeft: Float = 0f
    private var gridTop: Float = 0f
    private var gridRight: Float = 0f
    private var gridBottom: Float = 0f

    private var cellSize: Float = 0f
    private var cellSpacing: Float = 0f
    private var cellSpacingRatio: Float = .1f

    private val gridPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = 0xFF000000.toInt()
    }

    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = 0xFFFFFFFF.toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        recalculateDimensions(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(gridLeft, gridTop, gridRight, gridBottom, gridPaint)

        for (row in 0 until rows) {
            val cellTop = gridTop + cellSpacing + ((cellSize + cellSpacing) * row)

            for (column in 0 until columns) {
                val cellLeft = gridLeft + cellSpacing + ((cellSize + cellSpacing) * column)

                canvas?.drawRect(cellLeft, cellTop, cellLeft+cellSize, cellTop+cellSize, backgroundPaint)
            }
        }

        if (canvas != null) {
            val shipImageShortSize = cellSize * (1 - (cellSpacingRatio*2))
            val shipImageLongSide = cellSize * (1+cellSpacingRatio)

            val shipImageOffset = cellSize * (cellSpacingRatio)

            for (ship in shipsToDisplay) {
                val shipLeft = gridLeft + cellSpacing + ((cellSize + cellSpacing) * ship.second.left) + shipImageOffset
                val shipTop = gridTop + cellSpacing + ((cellSize + cellSpacing) * ship.second.top) + shipImageOffset
                val shipRight = shipLeft + shipImageShortSize + (shipImageLongSide * (ship.second.width - 1))
                val shipBottom = shipTop + shipImageShortSize + (shipImageLongSide * (ship.second.height - 1))

                drawableShips[ship.first].setBounds(shipLeft.toInt(), shipTop.toInt(), shipRight.toInt(), shipBottom.toInt())
                drawableShips[ship.first].draw(canvas)
            }
        }
    }

    private fun recalculateDimensions(w: Int = width, h: Int = height) {
        val diameterX = w/(columns + (columns+1)*cellSpacingRatio)
        val diameterY = h/(rows + (rows+1)*cellSpacingRatio)
        cellSize = minOf(diameterX, diameterY)
        cellSpacing = cellSize*cellSpacingRatio

        val gridWidth = columns * (cellSize+cellSpacing) + cellSpacing
        val gridHeight = rows * (cellSize+cellSpacing) + cellSpacing

        gridLeft = (w - gridWidth)/2
        gridRight = gridLeft + gridWidth
        gridTop = (h - gridHeight)/2
        gridBottom = gridTop + gridHeight
    }

}