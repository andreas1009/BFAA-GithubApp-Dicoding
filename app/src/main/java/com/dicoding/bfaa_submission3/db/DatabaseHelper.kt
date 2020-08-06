package com.dicoding.bfaa_submission3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.bfaa_submission3.db.DatabaseContract.UserColumns
import com.dicoding.bfaa_submission3.db.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbfavoriteuser"

        private const val DATABASE_VERSION = 2

        private val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${UserColumns.NAME} TEXT NOT NULL," +
                " ${UserColumns.USERNAME} TEXT NOT NULL," +
                " ${UserColumns.LOCATION} TEXT NOT NULL," +
                " ${UserColumns.IMAGE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}