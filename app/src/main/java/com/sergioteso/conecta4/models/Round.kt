package com.sergioteso.conecta4.models

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

/**
 * Clase que ronda que contiene el id de esta, un titulo, la fecha de cuando fue creada y su
 * tablero correspondiente.
 */
class Round(var rows: Int, var columns: Int) {
    var id: String
    var title: String
    var date: String
    var board: TableroC4
    lateinit var firstPlayerName: String
    lateinit var firstPlayerUUID: String
    lateinit var secondPlayerName: String
    lateinit var secondPlayerUUID: String

    constructor(filas: Int) : this(filas, columns = filas)

    init {
        id = UUID.randomUUID().toString()
        title = "ROUND ${id.toString().substring(19, 23).toUpperCase()}"
        date = Date().toString()
        board = TableroC4(rows, columns)
    }

    fun toJSONString(): String {
        val json = JSONObject()
        json.put("id", id)
        json.put("title", title)
        json.put("date", date)
        json.put("rows", rows)
        json.put("columns", columns)
        json.put("boardString", board.tableroToString())
        json.put("firstPlayerName", firstPlayerName)
        json.put("firstPlayerUUID", firstPlayerUUID)
        json.put("secondPlayerName", secondPlayerName)
        json.put("secondPlayerUUID", secondPlayerUUID)
        return json.toString()
    }


    companion object {
        fun fromJSONString(string: String): Round {
            val jsonObject = JSONObject(string)
            val rows = jsonObject.get("rows") as Int
            val columns = jsonObject.get("columns") as Int
            val round = Round(rows, columns)
            round.id = jsonObject.get("id") as String
            round.title = jsonObject.get("title") as String
            round.date = jsonObject.get("date") as String
            round.board.stringToTablero(jsonObject.get("boardString") as String)
            round.firstPlayerName = jsonObject.get("firstPlayerName") as String
            round.firstPlayerUUID = jsonObject.get("firstPlayerUUID") as String
            round.secondPlayerName = jsonObject.get("secondPlayerName") as String
            round.secondPlayerUUID = jsonObject.get("secondPlayerUUID") as String
            return round
        }
    }

}