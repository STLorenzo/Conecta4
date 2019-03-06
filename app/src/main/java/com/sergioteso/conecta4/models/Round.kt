package com.sergioteso.conecta4.models

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class Round(var tableroc4 : TableroC4) {
    var id = UUID.randomUUID().toString()
    var title = "ROUND ${id.toString().substring(19,23).toUpperCase()}\n"
    var date = Date().toString()
}