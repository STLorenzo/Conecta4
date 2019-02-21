package com.sergioteso.conecta4.models

import es.uam.eps.multij.Movimiento

/**
 * Clase que modeliza un movimiento en el juego Conecta4 extiendiendo Movimiento.
 *
 * @property columna columna en la cual se desea colocar una ficha
 */
class MovimientoC4(private val columna: Int) : Movimiento() {

    fun getColumna(): Int = columna

    /**
     * Override del toString de la clase Movimiento
     *
     * @return String con la información del movimiento
     */
    override fun toString(): String = "MovimientoC4| Columna: $columna"

    /**
     * Override del equals de la clase Movimiento
     *
     * @param other MovimientoC4 a comparar
     * @return Bool con la comparación entre los movimientos
     */
    override fun equals(other: Any?): Boolean =
        if (this === other) true
        else if (other == null || other !is MovimientoC4) false
        else this.columna == other.columna


    /**
     * Override del hashCode de la clase Movimiento
     *
     * @return hashCode de la clase MovimientoC4
     */
    override fun hashCode(): Int = columna.hashCode() * 31
}