package com.sergioteso.conecta4.models

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

/**
 * Clase que ronda que contiene el id de esta, un titulo, la fecha de cuando fue creada y su
 * tablero correspondiente.
 */
class Round(var tableroc4: TableroC4) {
    var id = UUID.randomUUID().toString()
    var title = "ROUND ${id.substring(19, 23).toUpperCase()}\n"
    var date = Date().toString()
}