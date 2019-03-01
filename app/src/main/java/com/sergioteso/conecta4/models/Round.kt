package com.sergioteso.conecta4.models

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class Round(var tableroc4 : TableroC4) {
    var id = UUID.randomUUID().toString()
    var title = "ROUND ${id.toString().substring(19,23).toUpperCase()}\n"
    var date = Date().toString()
    companion object {
        val CASILLA_J1 = 1
        val CASILLA_J2 = 2
        val CASILLA_WIN_J1 = 3
        val CASILLA_WIN_J2 = 4
    }
}