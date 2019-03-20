package com.sergioteso.conecta4.models

import kotlin.random.Random

object RoundRepository {
    var rounds = mutableListOf<Round>()

    init {
        for (i in 1..2)
            rounds.add(Round(TableroC4(Random.nextInt(4,7))))
    }

    fun addRound() {
        rounds.add(Round(TableroC4(Random.nextInt(4,7))))
    }

    fun getRound(round_id: String?):Round =
        rounds.filter { round -> round.id == round_id }.single()

}