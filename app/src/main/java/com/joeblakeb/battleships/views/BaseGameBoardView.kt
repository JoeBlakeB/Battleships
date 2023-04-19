package com.joeblakeb.battleships.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.RotateDrawable
import android.util.AttributeSet
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.joeblakeb.battleshipgame.Battleship
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleshipgame.Opponent
import com.joeblakeb.battleships.R
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent

val SHIP_SIZES: IntArray = intArrayOf(5,4,3,3,2)

abstract class BaseGameBoardView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    protected var gameBoard = GameBoard(Opponent.createRandomPlacement(SHIP_SIZES))

    private val shipsToDisplay: List<BattleshipOpponent.ShipInfo<Battleship>>
        get() = gameBoard.opponent.ships.mapIndexed {
                index, ship -> BattleshipOpponent.ShipInfo(index, ship)
        }

    private var drawableShips: Array<RotateDrawable> = arrayOf(
        RotateDrawable().apply { drawable = VectorDrawableCompat.create(resources, R.drawable.ship_carrier, null) },    // 5
        RotateDrawable().apply { drawable = VectorDrawableCompat.create(resources, R.drawable.ship_battleship, null) }, // 4
        RotateDrawable().apply { drawable = VectorDrawableCompat.create(resources, R.drawable.ship_submarine, null) },  // 3
        RotateDrawable().apply { drawable = VectorDrawableCompat.create(resources, R.drawable.ship_cruiser, null) },    // 3
        RotateDrawable().apply { drawable = VectorDrawableCompat.create(resources, R.drawable.ship_destroyer, null) }   // 2
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

    private var shipImageWidth: Float = 0f
    private var shipImageLength: Float = 0f
    private var shipImageOffset: Float = 0f

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
            for (ship in shipsToDisplay) {
                val shipLeft = gridLeft + cellSpacing + ((cellSize + cellSpacing) * ship.ship.left) + shipImageOffset
                val shipTop = gridTop + cellSpacing + ((cellSize + cellSpacing) * ship.ship.top) + shipImageOffset
                val shipRight = shipLeft + shipImageWidth + (shipImageLength * (ship.ship.width - 1))
                val shipBottom = shipTop + shipImageWidth + (shipImageLength * (ship.ship.height - 1))

                var rotateAmount = 0
                var horizontalChange = 0f
                if (ship.ship.height == 1) {
                    rotateAmount = 1
                    horizontalChange = (shipImageLength * (ship.ship.size - 1)) / 2
                }

                drawableShips[ship.index].toDegrees = 90f * rotateAmount
                drawableShips[ship.index].level = 10000 * rotateAmount
                drawableShips[ship.index].setBounds(
                    (shipLeft + horizontalChange).toInt(),
                    (shipTop - horizontalChange).toInt(),
                    (shipRight - horizontalChange).toInt(),
                    (shipBottom + horizontalChange).toInt()
                )

                drawableShips[ship.index].draw(canvas)
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

        shipImageWidth = cellSize * (1 - (cellSpacingRatio*2))
        shipImageLength = cellSize * (1+cellSpacingRatio)
        shipImageOffset = cellSize * (cellSpacingRatio)
    }

    /**
     * Get the grid coordinates of a location of the view,
     * used for knowing which ship the user presses on.
     *
     * @param x The x coordinate of the view
     * @param y The y coordinate of the view
     * @return The grid column and row, or null if outside of grid
     */
    protected fun gridCellAt(x: Float, y: Float): Pair<Int, Int>? {
        if (x in gridLeft .. gridRight && y in gridTop .. gridBottom ) {
            return Pair(
                ((x - gridLeft - cellSpacing) / (cellSize + cellSpacing)).toInt(),
                ((y - gridTop - cellSpacing) / (cellSize + cellSpacing)).toInt()
            )
        }
        return null
    }
}