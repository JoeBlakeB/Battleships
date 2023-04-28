package com.joeblakeb.battleships.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.joeblakeb.battleshipgame.Battleship
import com.joeblakeb.battleshipgame.GameBoard
import com.joeblakeb.battleships.R
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate

/**
 * The base code for all battleship game boards which draws a grid
 * with ships on top according to the shipsToDisplay list and
 * shots according to the gameBoard.
 * Also contains commonly used methods used by multiple specific
 * game board types.
 */
abstract class BaseGameBoardView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    abstract val gameBoard: GameBoard

    /** The ships that onDraw will show on the grid */
    protected open val shipsToDisplay: List<BattleshipOpponent.ShipInfo<Battleship>>
        get() = gameBoard.opponent.ships.mapIndexed {
                index, ship -> BattleshipOpponent.ShipInfo(index, ship)
        }

    /** Vectors for each of the ships in size order */
    private var drawableShips: Array<VectorDrawableCompat> = arrayOf(
        R.drawable.ship_carrier,    // 5
        R.drawable.ship_battleship, // 4
        R.drawable.ship_submarine,  // 3
        R.drawable.ship_cruiser,    // 3
        R.drawable.ship_destroyer   // 2
    ).map {
        VectorDrawableCompat.create(resources, it, null)!!.apply {
            setTintMode(PorterDuff.Mode.SRC_ATOP)
            mutate()
        }
    }.toTypedArray()

    /**
     * Vectors for drawing hit and miss markers,
     * miss is used for sunk as the ship will be red when sunk
     */
    private var drawableGuesses: Array<VectorDrawableCompat> = arrayOf(
        VectorDrawableCompat.create(resources, R.drawable.guess_miss, null)!!,
        VectorDrawableCompat.create(resources, R.drawable.guess_hit, null)!!
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
        color = ContextCompat.getColor(context, R.color.gridLines)
    }

    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.gridCell)
    }

    private val shipSunkTint: Int = ContextCompat.getColor(context, R.color.shipSunkTint)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        recalculateDimensions(w, h)
    }

    /**
     * Draws a grid, then ship vectors on top according to shipsToDisplay.
     *
     * Ship rotation done with help from Paul De Vrieze.
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        canvas.drawRect(gridLeft, gridTop, gridRight, gridBottom, gridPaint)

        // Draw Grid
        for (row in 0 until rows) {
            val cellTop = gridTop + cellSpacing + ((cellSize + cellSpacing) * row)

            for (column in 0 until columns) {
                val cellLeft = gridLeft + cellSpacing + ((cellSize + cellSpacing) * column)

                canvas.drawRect(cellLeft, cellTop, cellLeft+cellSize, cellTop+cellSize, backgroundPaint)
            }
        }

        // Draw Ships
        for (ship in shipsToDisplay) {
            val shipLeft = gridLeft + cellSpacing + ((cellSize + cellSpacing) * ship.ship.left) + shipImageOffset
            val shipTop = gridTop + cellSpacing + ((cellSize + cellSpacing) * ship.ship.top) + shipImageOffset
            val shipRight = shipLeft + shipImageWidth + (shipImageLength * (ship.ship.width - 1))

            val shipDrawable = drawableShips[ship.index]

            if (gameBoard.shipsSunk[ship.index]) {
                shipDrawable.setTint(shipSunkTint)
            } else {
                shipDrawable.setTint(0)
            }

            if (ship.ship.height == 1) {
                canvas.withTranslation(shipRight, shipTop) {
                    canvas.withRotation(90f) {
                        shipDrawable.setBounds(0, 0, shipImageWidth.toInt(), (shipImageWidth + (shipImageLength * (ship.ship.width - 1))).toInt())
                        shipDrawable.draw(this)
                    }
                }
            } else {
                canvas.withTranslation(shipLeft, shipTop) {
                    shipDrawable.setBounds(0, 0, shipImageWidth.toInt(), (shipImageWidth + (shipImageLength * (ship.ship.height - 1))).toInt())
                    shipDrawable.draw(this)
                }
            }
        }

        // Draw Guess Results
        for (row in 0 until rows) {
            val hitTop = gridTop + cellSpacing + ((cellSize + cellSpacing) * row)

            for (column in 0 until columns) {
                val guessDrawable = drawableGuesses[when(gameBoard[column, row]) {
                    is GuessCell.MISS -> 0
                    is GuessCell.HIT -> 1
                    is GuessCell.SUNK -> 0
                    else -> continue
                }]

                val hitLeft = gridLeft + cellSpacing + ((cellSize + cellSpacing) * column)

                guessDrawable.setBounds(hitLeft.toInt(), hitTop.toInt(), (hitLeft + cellSize).toInt(), (hitTop + cellSize).toInt())
                guessDrawable.draw(canvas)
            }
        }
    }

    /**
     * Recalculate dimension values that can be reused each time
     * the grid, ships, and shots are drawn.
     */
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
    protected fun gridCellAt(x: Float, y: Float): Coordinate? {
        if (x in gridLeft .. gridRight && y in gridTop .. gridBottom ) {
            return Coordinate(
                ((x - gridLeft - cellSpacing) / (cellSize + cellSpacing)).toInt(),
                ((y - gridTop - cellSpacing) / (cellSize + cellSpacing)).toInt()
            )
        }
        return null
    }
}