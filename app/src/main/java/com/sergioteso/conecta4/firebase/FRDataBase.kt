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
import android.R.string.cancel
import android.app.AlertDialog
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import com.sergioteso.conecta4.activities.SettingsActivityC4


class FRDataBase(var context: Context) : RoundRepository {
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
        val table = RoundDataBaseSchema.UserTable
        val cols = RoundDataBaseSchema.UserTable.Cols
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(playername, password).addOnCompleteListener {
            it.addOnSuccessListener {
                db.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.d("DEBUG", p0.toString())
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var uuid: String?
                        for (postSnapshot in dataSnapshot.child(table.NAME).children) {
                            val name: String = (postSnapshot.child(cols.PLAYERNAME).value) as String
                            if (name == playername) {
                                uuid = (postSnapshot.child(cols.PLAYERUUID).value) as String
                                Log.d("DEBUG", "login con exito")
                                callback.onLogin(uuid)
                            }
                        }
                    }
                })
            }
            it.addOnFailureListener {
                callback.onError(LOGIN_CREDENTIALS_ERROR)
            }
        }
    }

    companion object ErrorCodes {
        val REGISTER_ERROR = "reg_error"
        val REGISTER_ALREADY_EXISTS = "reg_exists"
        val LOGIN_CREDENTIALS_ERROR = "log_cre_error"
    }

    override fun register(playername: String, password: String, callback: RoundRepository.LoginRegisterCallback) {
        val table = RoundDataBaseSchema.UserTable
        val cols = RoundDataBaseSchema.UserTable.Cols
        val uuid = UUID.randomUUID().toString()

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("DEBUG", p0.toString())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var flag = true
                for (postSnapshot in dataSnapshot.child(table.NAME).children) {
                    val name = (postSnapshot.child(cols.PLAYERNAME).value) as String
                    if (name == playername) {
                        callback.onError(REGISTER_ALREADY_EXISTS)
                        flag = false
                    }
                }
                if (flag) {
                    createEmailPassword(playername, password, uuid, callback)
                }
            }
        })
    }

    fun createEmailPassword(
        email: String,
        password: String,
        uuid: String,
        callback: RoundRepository.LoginRegisterCallback
    ) {
        val table = RoundDataBaseSchema.UserTable
        val cols = RoundDataBaseSchema.UserTable.Cols
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            it.addOnSuccessListener {
                db.child(table.NAME).child(uuid)
                db.child(table.NAME).child(uuid).child(cols.PLAYERNAME).setValue(email)
                db.child(table.NAME).child(uuid).child(cols.PLAYERPASSWORD).setValue(password)
                db.child(table.NAME).child(uuid).child(cols.PLAYERUUID).setValue(uuid)
                Log.d("DEBUG", "registrado con exito")
                callback.onLogin(uuid)
            }

            it.addOnFailureListener {
                callback.onError(REGISTER_ERROR)
            }
        }
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
                val rounds = mutableListOf<Round>()
                for (postSnapshot in dataSnapshot.children) {
                    val round_string = postSnapshot.value as String?
                    if (round_string != null) {
                        val round = Round.fromJSONString(round_string)
                        if (isOpenOrIamIn(round))
                            rounds.add(round)
                    }
                }
                callback.onResponse(rounds)
            }
        })
    }

    fun isOpenOrIamIn(round: Round): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        if (round.firstPlayerName == user?.email || round.secondPlayerName == user?.email)
            return true
        return false
    }

    override fun addRound(round: Round, callback: RoundRepository.BooleanCallback) {
        val table = RoundDataBaseSchema.RoundTable
        val task = db.child(table.NAME).child(round.id).setValue(round.toJSONString())

        task.addOnSuccessListener {
            callback.onResponse(true)
        }
        task.addOnFailureListener {
            callback.onResponse(false)
        }
    }

    override fun updateRound(round: Round, callback: RoundRepository.BooleanCallback) {
        val table = RoundDataBaseSchema.RoundTable

        db.child(table.NAME).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("DEBUG", p0.toString())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val roundId = postSnapshot.key
                    if (roundId == round.id) {
                        val task = db.child(table.NAME).child(round.id).setValue(round.toJSONString())

                        task.addOnSuccessListener {
                            callback.onResponse(true)
                        }
                        task.addOnFailureListener {
                            callback.onResponse(false)
                        }
                    }
                }
            }
        })
    }

    override fun createRound(rows: Int, columns: Int, context: Context, callback: RoundRepository.BooleanCallback){
        Log.d("DEBUG","createround")
        var email: String
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Insert Email of the player")

        // Set up the input
        val input = EditText(context)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK")
        { _, _ ->
            email = input.text.toString()
            val uuid = getUUIDfromEmail(email)
        }
        builder.setNegativeButton("Cancel")
        { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    fun getUUIDfromEmail(email: String):String?{
        val table = RoundDataBaseSchema.UserTable
        val cols = RoundDataBaseSchema.UserTable.Cols
        var uuid :String? = null
        db.child(table.NAME).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("DEBUG", p0.toString())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val email2 = postSnapshot.child(cols.PLAYERNAME).value as String
                    Log.d("DEBUG",email2)
                    if(email2 == email){
                        uuid = postSnapshot.child(cols.PLAYERUUID).value as String
                        break
                    }
                }
            }
        })
        Log.d("DEBUG",uuid)
        return uuid
    }
}