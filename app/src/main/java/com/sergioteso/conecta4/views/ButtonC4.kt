package com.sergioteso.conecta4.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.Button
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.update
import com.sergioteso.conecta4.models.TableroC4

class ButtonC4(context: Context) : Button(context){
    private var radious: Float = 0.toFloat()
    private var currentColor = Color.GRAY
    private val paint: Paint
    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = 0.5f * width
        val y = 0.5f * height
        width = if (width < height) width else height
        radious = 0.15f * width
        paint.setColor(currentColor)
        canvas.drawCircle(x, y, radious, paint)
    }


    fun setBackgroundCasilla(player: Int){
        when (player) {
            TableroC4.CASILLA_J1 -> currentColor = ContextCompat.getColor(context, R.color.darkGreen)
            TableroC4.CASILLA_J2 -> currentColor = ContextCompat.getColor(context, R.color.darkRed)
            TableroC4.CASILLA_WIN_J1 -> currentColor = ContextCompat.getColor(context, R.color.lightGreen)
            TableroC4.CASILLA_WIN_J2 -> currentColor = ContextCompat.getColor(context, R.color.lightRed)
            else -> currentColor = Color.GRAY
        }
        invalidate()
    }

    fun setBackgroundCasilla2(player: Int){
        when (player) {
            TableroC4.CASILLA_J1 -> this.setBackgroundResource(R.drawable.casilla_verde)
            TableroC4.CASILLA_J2 -> this.setBackgroundResource(R.drawable.casilla_roja)
            TableroC4.CASILLA_WIN_J1 -> this.setBackgroundResource(R.drawable.casilla_win_verde)
            TableroC4.CASILLA_WIN_J2 -> this.setBackgroundResource(R.drawable.casilla_win_roja)
            else -> this.setBackgroundResource(R.drawable.casilla_vacia)
        }
        invalidate()
    }
}