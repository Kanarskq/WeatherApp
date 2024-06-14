package com.example.weatherapp.repositories

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.weatherapp.db.UserDb
import com.example.weatherapp.dtos.User
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(context: Context) {
    private val dbHelper = UserDb(context)
    private val gson = Gson()

    suspend fun addUser(user: User): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val cursor: Cursor = db.query(
            UserDb.TABLE_USERS,
            arrayOf(UserDb.COLUMN_ID),
            "${UserDb.COLUMN_EMAIL} = ?",
            arrayOf(user.email),
            null, null, null
        )

        val emailExists = cursor.count > 0
        cursor.close()

        if (emailExists) {
            db.close()
            return@withContext false
        }

        val values = ContentValues().apply {
            put(UserDb.COLUMN_EMAIL, user.email)
            put(UserDb.COLUMN_PASSWORD, user.password)
            put(UserDb.COLUMN_TEMP_UNIT, user.tempUnit)
            put(UserDb.COLUMN_FAVORITE_CITIES, gson.toJson(user.favoriteCities))
        }
        val result = db.insert(UserDb.TABLE_USERS, null, values)
        db.close()
        return@withContext result != -1L
    }

    suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            UserDb.TABLE_USERS,
            arrayOf(
                UserDb.COLUMN_ID,
                UserDb.COLUMN_EMAIL,
                UserDb.COLUMN_PASSWORD,
                UserDb.COLUMN_TEMP_UNIT,
                UserDb.COLUMN_FAVORITE_CITIES
            ),
            "${UserDb.COLUMN_EMAIL} = ?",
            arrayOf(email),
            null, null, null
        )
        var user: User? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(UserDb.COLUMN_ID))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(UserDb.COLUMN_EMAIL))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(UserDb.COLUMN_PASSWORD))
            val tempUnit = cursor.getString(cursor.getColumnIndexOrThrow(UserDb.COLUMN_TEMP_UNIT))
            val favoriteCitiesJson = cursor.getString(cursor.getColumnIndexOrThrow(UserDb.COLUMN_FAVORITE_CITIES))
            val favoriteCities = gson.fromJson(favoriteCitiesJson, Array<String>::class.java).toList()
            user = User(id, userEmail, password, tempUnit, favoriteCities)
        }
        cursor.close()
        db.close()
        return@withContext user
    }

    suspend fun updateUser(user: User): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(UserDb.COLUMN_EMAIL, user.email)
            put(UserDb.COLUMN_PASSWORD, user.password)
            put(UserDb.COLUMN_TEMP_UNIT, user.tempUnit)
            put(UserDb.COLUMN_FAVORITE_CITIES, gson.toJson(user.favoriteCities))
        }
        val result = db.update(UserDb.TABLE_USERS, values, "${UserDb.COLUMN_ID} = ?", arrayOf(user.id.toString()))
        db.close()
        return@withContext result > 0
    }
}