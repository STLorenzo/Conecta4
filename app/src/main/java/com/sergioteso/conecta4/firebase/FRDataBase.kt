package com.sergioteso.conecta4.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sergioteso.conecta4.database.RoundDataBaseSchema
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.RoundRepository
import java.lang.Exception
import java.util.*

class FRDataBase(var context: Context): RoundRepository {
    private val DATABASENAME = "partidas"
    lateinit var db: DatabaseReference

    fun startListeningChanges(callback: RoundRepository.RoundsCallback) {
        db = FirebaseDatabase.getInstance().getReference().child(DATABASENAME)
        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("DEBUG", p0.toString())
            }
            override fun onDataChange(p0: DataSnapshot) {
                var rounds = listOf<Round>()
                for (postSnapshot in p0.children)
                    rounds += postSnapshot.getValue(Round::class.java)!!
                callback.onResponse(rounds)
            }
        })
    }

    //fun startListeningBoardChanges(callback: RoundRepository.RoundsCallback) { ... }

    override fun open() {
        db = FirebaseDatabase.getInstance().reference.child(DATABASENAME)
    }

    override fun close() {
        //En teoria se queda vacio
    }

    override fun login(playername: String, password: String, callback: RoundRepository.LoginRegisterCallback) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(playername, password).addOnCompleteListener {
            if (it.isSuccessful){
                callback.onLogin(playername)
            }else{
                callback.onError("Username or password incorrect.")
            }
        }
    }

    override fun register(playername: String, password: String, callback: RoundRepository.LoginRegisterCallback) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val sh = RoundDataBaseSchema.UserTable
        //val uuid = UUID.randomUUID().toString()

        firebaseAuth.createUserWithEmailAndPassword(playername, password)
        try{
            db.child(sh.NAME).child(playername)
            db.child(sh.NAME).child(playername).child(RoundDataBaseSchema.UserTable.Cols.PLAYERNAME).setValue(playername)
            db.child(sh.NAME).child(playername).child(RoundDataBaseSchema.UserTable.Cols.PLAYERPASSWORD).setValue(password)
        }catch (e: Exception){
            callback.onError("Error registering player")
        }
        Log.d("DEBUG","registrado con exito")
        callback.onLogin(playername)
    }

    override fun getRounds(
        playeruuid: String,
        orderByField: String,
        group: String,
        callback: RoundRepository.RoundsCallback
    ) {
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("DEBUG", p0.toString())
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var rounds = listOf<Round>()
//                for (postSnapshot in dataSnapshot.children) {
//                    val round = postSnapshot.getValue(Round::class.java)!!
//                    if (isOpenOrIamIn(round))
//                        rounds += round
//                }
                callback.onResponse(rounds)
            }
        })

    }

    override fun addRound(round: Round, callback: RoundRepository.BooleanCallback) {
        if (db.child(round.id).setValue(round).isSuccessful)
            callback.onResponse(true)
        else
            callback.onResponse(false)
    }

    override fun updateRound(round: Round, callback: RoundRepository.BooleanCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}