package com.sergioteso.conecta4.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.RoundRepository
import com.sergioteso.conecta4.database.RoundDataBaseSchema.UserTable
import com.sergioteso.conecta4.database.RoundDataBaseSchema.RoundTable
import java.sql.SQLException

class DatabaseC4(context: Context) : RoundRepository {
    private val DEBUG_TAG = "DEBUG"
    private val helper: DatabaseHelper
    private var db: SQLiteDatabase? = null

    init {
        helper = DatabaseHelper(context)
    }



    private class DatabaseHelper(context: Context) :
            SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            createTable(db)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS "+ UserTable.NAME)
            db.execSQL("DROP TABLE IF EXISTS "+ RoundTable.NAME)
            createTable(db)
        }

        private fun createTable(db: SQLiteDatabase) {
            val str1 = ("CREATE TABLE " + UserTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + UserTable.Cols.PLAYERUUID + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERNAME + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERPASSWORD + " TEXT);")
            val str2 = ("CREATE TABLE " + RoundTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + RoundTable.Cols.ROUNDUUID + " TEXT UNIQUE, "
                    + RoundTable.Cols.PLAYERUUID + " TEXT REFERENCES "
                    + UserTable.Cols.PLAYERUUID + ", "
                    + RoundTable.Cols.DATE + " TEXT, "
                    + RoundTable.Cols.TITLE + " TEXT, "
                    + RoundTable.Cols.SIZE + " TEXT, "
                    + RoundTable.Cols.BOARD + " TEXT);")

            try{
                db.execSQL(str1)
                db.execSQL(str2)
            }catch (e: SQLException){
                e.printStackTrace()
            }

        }
    }

    override fun open() {
        db = helper.writableDatabase
    }

    override fun close() {
        db?.close()
    }

    override fun login(playername: String, password: String, callback: RoundRepository.LoginRegisterCallback) {
        Log.d(DEBUG_TAG, "Login $playername")
        val cursor =  db!!.query(UserTable.NAME,
                                    arrayOf(UserTable.Cols.PLAYERUUID),
                                    UserTable.Cols.PLAYERNAME + " = ? AND "
                                    + UserTable.Cols.PLAYERPASSWORD + " = ?",
                                    arrayOf(playername, password),
                                    null, null, null)

        val count = cursor.count
        val uuid = if (count == 1 && cursor.moveToFirst())
                        cursor.getString(0) else ""
        cursor.close()
        if( count == 1)
            callback.onLogin(uuid)
        else
            callback.onError("Username or password incorrect.")
    }

    override fun register(playername: String, password: String, callback: RoundRepository.LoginRegisterCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun addRound(round: Round, callback: RoundRepository.BooleanCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateRound(round: Round, callback: RoundRepository.BooleanCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRounds(playeruuid: String, orderByField: String,
                           group: String, callback: RoundRepository.RoundsCallback) {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val DATABASE_NAME = "c4.db"
        private val DATABASE_VERSION = 1
    }
}