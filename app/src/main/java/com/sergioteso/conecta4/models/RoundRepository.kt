package com.sergioteso.conecta4.models

object RoundRepository {
    var rounds = mutableListOf<Round>()

    init {
        for (i in 1..100)
            rounds.add(Round())
    }
}