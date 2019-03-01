package com.sergioteso.conecta4.models

import kotlin.random.Random

object RoundRepository {
    var rounds = mutableListOf<Round>()

    init {
        for (i in 1..100)
            rounds.add(Round(TableroC4(Random.nextInt(4,7))))
    }
}