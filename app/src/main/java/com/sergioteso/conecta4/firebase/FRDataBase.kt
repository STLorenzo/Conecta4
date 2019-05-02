package com.sergioteso.conecta4.firebase

import android.content.Context
import android.util.Log
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
        Log.d("DEBUG", "Empieza Login")
        db.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("DEBUG", p0.toString())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("DEBUG", "Empieza onDataCHange")
                var uuid: String?
                val table = RoundDataBaseSchema.UserTable
                val cols = RoundDataBaseSchema.UserTable.Cols
                var flag = true

                for (postSnapshot in dataSnapshot.child(table.NAME).children){
                    val name : String = (postSnapshot.child(cols.PLAYERNAME).value) as String
                    if ( name == playername ){
                        val pass : String = (postSnapshot.child(cols.PLAYERPASSWORD).value) as String
                        if( pass == password) {
                            uuid = (postSnapshot.child(cols.PLAYERUUID).value) as String
                            flag = false
                            callback.onLogin(uuid)
                        }


                    }
                }
                if(flag) callback.onError(LOGIN_CREDENTIALS_ERROR)
            }

        })
    }

    companion object ErrorCodes{
        val REGISTER_ERROR = "reg_error"
        val REGISTER_ALREADY_EXISTS = "reg_exists"
        val LOGIN_CREDENTIALS_ERROR = "log_cre_error"
    }

    override fun register(playername: String, password: String, callback: RoundRepository.LoginRegisterCallback) {
        val table = RoundDataBaseSchema.UserTable
        val cols = RoundDataBaseSchema.UserTable.Cols
        val uuid = UUID.randomUUID().toString()

        db.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("DEBUG", p0.toString())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var flag = true
                    for (postSnapshot in dataSnapshot.child(table.NAME).children){
                        val name = (postSnapshot.child(cols.PLAYERNAME).value) as String
                        if (name == playername){
                            Log.d("DEBUG", REGISTER_ALREADY_EXISTS)
                            callback.onError(REGISTER_ALREADY_EXISTS)
                            flag = false
                        }
                    }
                    if (flag){
                        db.child(table.NAME).child(playername)
                        db.child(table.NAME).child(playername).child(cols.PLAYERNAME).setValue(playername)
                        db.child(table.NAME).child(playername).child(cols.PLAYERPASSWORD).setValue(password)
                        db.child(table.NAME).child(playername).child(cols.PLAYERUUID).setValue(uuid)
                        Log.d("DEBUG","registrado con exito")
                        callback.onLogin(uuid)
                    }
                }
            })
    }

    override fun getRounds(
        playeruuid: String,
        orderByField: String,
        group: String,
        callback: RoundRepository.RoundsCallback
    ) {
        val table = RoundDataBaseSchema.RoundTable
        db.child(table.NAME).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("DEBUG", p0.toString())
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var rounds = listOf<Round>()
                for (postSnapshot in dataSnapshot.children) {
                    val round_string = postSnapshot.value as String?
                    if(round_string != null){
                        val round = Round.fromJSONString(round_string)
                        if (isOpenOrIamIn(round))
                            rounds += round
                    }
                }
                callback.onResponse(rounds)
            }
        })
    }

    fun isOpenOrIamIn(round: Round): Boolean{
        return true
    }

    override fun addRound(round: Round, callback: RoundRepository.BooleanCallback) {
        val table = RoundDataBaseSchema.RoundTable
        Log.d("DEBUG", round.board.toString())
        if (db.child(table.NAME).child(round.id).setValue(round.toJSONString()).isSuccessful)
            callback.onResponse(true)
        else
            callback.onResponse(false)
    }

    override fun updateRound(round: Round, callback: RoundRepository.BooleanCallback) {
        val table = RoundDataBaseSchema.RoundTable
        db.child(table.NAME).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("DEBUG", p0.toString())
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val roundId = postSnapshot.value as String?
                    if( roundId == round.id){
                        if (db.child(table.NAME).child(round.id).setValue(round.toJSONString()).isSuccessful)
                            callback.onResponse(true)
                        else
                            callback.onResponse(false)
                    }
                }
            }
        })

    }
}