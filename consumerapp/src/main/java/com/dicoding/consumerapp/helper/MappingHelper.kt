package com.dicoding.consumerapp.helper

import android.database.Cursor
import com.dicoding.consumerapp.db.DatabaseContract
import com.dicoding.consumerapp.model.UserEntity
import java.util.*

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<UserEntity> {
        val userList = ArrayList<UserEntity>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val location =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val image = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.IMAGE))
                userList.add(UserEntity(id, name, username, location, image))
            }
        }
        return userList
    }

    fun mapCursorToObject(userCursor: Cursor?): UserEntity {
        var user = UserEntity()
        userCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
            val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
            val image = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.IMAGE))
            user = UserEntity(id, name, username, location, image)
        }
        return user
    }
}