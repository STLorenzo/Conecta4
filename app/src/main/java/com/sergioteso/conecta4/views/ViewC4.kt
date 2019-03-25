package com.sergioteso.conecta4.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sergioteso.conecta4.activities.setColor
import com.sergioteso.conecta4.models.TableroC4

class ViewC4(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val DEBUG = "ViewC4"
    private var numero: Int = 0
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var heightOfTile: Float = 0.toFloat()
    private var widthOfTile: Float = 0.toFloat()
    private var radio: Float = 0.toFloat()
    private var size: Int = 0
    private var board: TableroC4? = null

    init {
        backgroundPaint.color = Color.BLACK
        linePaint.strokeWidth = 2f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 500
        val wMode: String
        val hMode: String
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
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
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val boardWidth = width.toFloat()
        val boardHeight = height.toFloat()
        canvas.drawRect(0f, 0f, boardWidth, boardHeight, backgroundPaint)
        drawCircles(canvas, linePaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthOfTile = (w / size).toFloat()
        heightOfTile = (h / size).toFloat()
        if (widthOfTile < heightOfTile)
            radio = widthOfTile * 0.3f
        else
            radio = heightOfTile * 0.3f
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun drawCircles(canvas: Canvas, paint: Paint) {
        var centerRaw: Float
        var centerColumn: Float
        for (i in 0 until size) {
            val pos = size - i - 1
            centerRaw = heightOfTile * (1 + 2 * pos) / 2f
            for (j in 0 until size) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f
                paint.setColor(board!!, i, j,context)
                canvas.drawCircle(centerColumn, centerRaw, radio, paint)
            }
        }
    }

}
