package com.example.foodjoa.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true) val uId: Int,
    @ColumnInfo(name="userEmail") val userEmail: String,
    @ColumnInfo(name="userId") val userId: String?,
    @ColumnInfo(name="userNickname") val userNickname: String,
    @ColumnInfo(name="userPassword") val userPassword: String,
    @ColumnInfo(name="hour") val hour: Int,
    @ColumnInfo(name="minute") val minute: Int,
    @ColumnInfo(name="checked") val checked: Boolean,
)