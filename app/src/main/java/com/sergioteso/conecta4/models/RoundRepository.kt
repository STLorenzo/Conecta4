package com.sergioteso.conecta4.models

import android.content.Context
import java.lang.Exception
import kotlin.random.Random

///**
// * Objeto que contiene todas las rondas de la aplicacion y da acceso universal a todas las clases
// */
//object RoundRepository {
//    var rounds = mutableListOf<Round>()
//
//    /**
//     * Inializa el objeto creando un numero minimo de rondas
//     */
//    init {
//        for (i in 1..2)
//            rounds.add(Round(TableroC4(Random.nextInt(4, 7))))
//    }
//
//    /**
//     * Añade una ronda al repositorio
//     */
//    fun addRound() {
//        rounds.add(Round(TableroC4(Random.nextInt(4, 7))))
//    }
//
//    fun addRound(rows: Int, columns: Int) {
//        rounds.add(Round(TableroC4(rows, columns)))
//    }
//
//    /**
//     * Obtiene la ronda con id round_id del repositorio
//     */
//    fun getRound(round_id: String?): Round =
//        rounds.filter { round -> round.id == round_id }.single()
//
//}

interface RoundRepository{
    @Throws(Exception::class)
    fun open()
    fun close()

    interface LoginRegisterCallback {
        fun onLogin(playerUuid: String)
        fun onError(error: String)
    }

    fun login(playername: String, password: String, callback: LoginRegisterCallback)
    fun register(playername: String, password: String, callback: LoginRegisterCallback)

    interface BooleanCallback {
        fun onResponse(ok: Boolean)
    }

    fun createRound(rows: Int, columns: Int, context: Context, callback: BooleanCallback)
    fun getRounds(playeruuid: String, orderByField: String, group: String,
                  callback: RoundsCallback)
    fun addRound(round: Round, callback: BooleanCallback)
    fun updateRound(round: Round, callback: BooleanCallback)

    interface RoundsCallback {
        fun onResponse(rounds: List<Round>)
        fun onError(error: String)
    }
}