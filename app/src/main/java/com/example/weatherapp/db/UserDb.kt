package com.example.weatherapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "users.db"
private const val DATABASE_VERSION = 2

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${UserDb.TABLE_USERS} (" +
            "${UserDb.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${UserDb.COLUMN_EMAIL} TEXT NOT NULL," +
            "${UserDb.COLUMN_PASSWORD} TEXT NOT NULL," +
            "${UserDb.COLUMN_TEMP_UNIT} TEXT NOT NULL DEFAULT 'celsius'," +
            "${UserDb.COLUMN_FAVORITE_CITIES} TEXT NOT NULL DEFAULT '[]')"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${UserDb.TABLE_USERS}"

class UserDb(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_TEMP_UNIT = "temp_unit"
        const val COLUMN_FAVORITE_CITIES = "favorite_cities"
    }
}