package com.sergioteso.conecta4.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.*
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Logger.log
import com.sergioteso.conecta4.activities.setColor
import com.sergioteso.conecta4.models.TableroC4
import es.uam.eps.multij.Tablero

class ViewC4(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var heightOfTile: Float = 0.toFloat()
    private var widthOfTile: Float = 0.toFloat()
    private var radio: Float = 0.toFloat()
    private var columns: Int = 4
    private var rows: Int = 6
    private var board: TableroC4? = null
    private var onPlayListener: OnPlayListener? = null
    private var myGestureDetector: GestureDetector

    interface OnPlayListener {
        fun onPlay(column: Int)
    }

    init {
        backgroundPaint.color = Color.BLACK
        linePaint.strokeWidth = 2f
        myGestureDetector = GestureDetector(context, MyGestureListener())
    }

    internal inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            Toast.makeText(context, "onDown: " + e.toString(), Toast.LENGTH_SHORT).show()
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Toast.makeText(context, "onDoubleTap: " + e.toString(), Toast.LENGTH_SHORT).show()
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Toast.makeText(
                context, "onFling: " + e1.toString() + e2.toString(),
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val desiredWidth = 500
//        val wMode: String
//        val hMode: String
//        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
//        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int

        if (widthSize < heightSize) {
            heightSize = widthSize
            height = heightSize
            width = height
        } else {
            widthSize = heightSize
            height = widthSize
            width = height
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val boardWidth = width.toFloat()
        val boardHeight = height.toFloat()
        canvas.drawRect(0f, 0f, boardWidth, boardHeight, backgroundPaint)
        drawCircles(canvas, linePaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthOfTile = (w / columns).toFloat()
        heightOfTile = (h / rows).toFloat()
        if (widthOfTile < heightOfTile)
            radio = widthOfTile * 0.3f
        else
            radio = heightOfTile * 0.3f
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun drawCircles(canvas: Canvas, paint: Paint) {
        var centerRow: Float
        var centerColumn: Float
        for (i in 0 until rows) {
            val pos = rows - i - 1
            centerRow = heightOfTile * (1 + 2 * pos) / 2f
            for (j in 0 until columns) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f
                paint.setColor(board!!, rows - 1 - i, j, context)
                //paint.color = Color.RED
                canvas.drawCircle(centerColumn, centerRow, radio, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        myGestureDetector.onTouchEvent(event)
//        return true
        if (onPlayListener == null)
            return super.onTouchEvent(event)
        if (board!!.estado != Tablero.EN_CURSO) {
            Snackbar.make(
                this,
                R.string.round_already_finished, Snackbar.LENGTH_SHORT
            ).show()
            return super.onTouchEvent(event)
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            try {
                onPlayListener?.onPlay(fromEventToJ(event))
            } catch (e: Exception) {
                Toast.makeText(context, "Jugada inválida", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun fromEventToJ(event: MotionEvent): Int {
        return (event.x / widthOfTile).toInt()
    }

    fun setOnPlayListener(listener: OnPlayListener) {
        this.onPlayListener = listener
    }

    fun setBoard(board: TableroC4) {
        this.board = board
        this.rows = board.filas
        this.columns = board.columnas
    }

}
