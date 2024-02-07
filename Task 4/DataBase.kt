package com.example.zadanie1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBase(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,
    null, DATABASE_VER) {
    companion object {
        private val DATABASE_VER = 2
        private val DATABASE_NAME = "users.db"

        //Table
        private val TABLE_NAME = "USERS"
        private val COL_ID = "ID"
        private val COL_NICK = "NICK"
        private val COL_PASSWORD = "PASSWORD"
        private val COL_SCORE="SCORE";
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NICK TEXT, $COL_PASSWORD TEXT, $COL_SCORE INTEGER)")
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db!!)
    }
    fun addUser(username: String, password: String): Long {
        val db= this.writableDatabase
        val values = ContentValues()
        values.put(COL_NICK, username)
        values.put(COL_PASSWORD, password)
        values.put(COL_SCORE, 0)

        val selectQuery="SELECT * FROM $TABLE_NAME WHERE $COL_NICK=?";

        val cursor = db.rawQuery(selectQuery, arrayOf(username))
        if(cursor.moveToFirst()){
            db.close()
            return 0
        } else {
            val result = db.insert(TABLE_NAME, null, values)
            db.close()
            return result
        }
    }

    @SuppressLint("Range")
    fun showUser(): ArrayList<String> {
        val users: ArrayList<String> = ArrayList()
        val selectQuery="SELECT $COL_NICK, $COL_SCORE FROM $TABLE_NAME ORDER BY $COL_SCORE DESC LIMIT 10";
        val db= this.readableDatabase
        val cursor : Cursor?

        try {
            cursor =  db.rawQuery(selectQuery, null)

        } catch(e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var username: String
        var score: Int

        if(cursor.moveToFirst()) {
            do {
                username = cursor.getString(cursor.getColumnIndex(COL_NICK)).toString()
                score=cursor.getInt(cursor.getColumnIndex(COL_SCORE))
                users.add("Użytkownik: $username \nPunkty: $score")
            } while (cursor.moveToNext());
        }
        db.close()
        return users
    }
    @SuppressLint("Range")
    fun checkUser(username: String, password: String): Int {
        val selectQuery="SELECT $COL_NICK, $COL_PASSWORD FROM $TABLE_NAME";
        val db= this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var usernameDB : String
        var passwordDB : String
        if(cursor.moveToFirst()) {
            do {
                usernameDB = cursor.getString(cursor.getColumnIndex(COL_NICK)).toString()
                passwordDB = cursor.getString(cursor.getColumnIndex(COL_PASSWORD)).toString()
                if(username==usernameDB && password==passwordDB){
                    return 1
                }
            } while (cursor.moveToNext());
        }
        return 0
    }

    @SuppressLint("Range")
    fun getScore(username: String): Int{
        val selectQuery="SELECT $COL_SCORE FROM $TABLE_NAME WHERE $COL_NICK=?"
        val db= this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(username))
        var score=0
        if(cursor.moveToFirst()){
            score=cursor.getInt(cursor.getColumnIndex(COL_SCORE))
        }
        db.close()
        return score
    }
    fun updateScore(username: String, score: Int) {
        val selectQuery="UPDATE $TABLE_NAME SET $COL_SCORE=? WHERE $COL_NICK=?"
        val db= this.writableDatabase
        var result=db.execSQL(selectQuery, arrayOf(score, username))
        db.close()
        return result
    }
}

