package com.sergioteso.conecta4.models

import kotlin.random.Random

/**
 * Objeto que contiene todas las rondas de la aplicacion y da acceso universal a todas las clases
 */
object RoundRepository {
    var rounds = mutableListOf<Round>()

    /**
     * Inializa el objeto creando un numero minimo de rondas
     */
    init {
        for (i in 1..2)
            rounds.add(Round(TableroC4(Random.nextInt(4, 7))))
    }

    /**
     * Añade una ronda al repositorio
     */
    fun addRound() {
        rounds.add(Round(TableroC4(Random.nextInt(4, 7))))
    }

    /**
     * Obtiene la ronda con id round_id del repositorio
     */
    fun getRound(round_id: String?): Round =
        rounds.filter { round -> round.id == round_id }.single()

}