package com.sergioteso.conecta4.models

import android.content.Context
import com.sergioteso.conecta4.database.DatabaseC4
import java.lang.Exception

object RoundRepositoryFactory {
    private val LOCAL = true
    fun createRepository(context: Context): RoundRepository? {
        val repository: RoundRepository
        repository = if (LOCAL) DatabaseC4(context) else DatabaseC4(context)
        try{
            repository.open()
        } catch (e: Exception){
            return null
        }
        return repository
    }
}