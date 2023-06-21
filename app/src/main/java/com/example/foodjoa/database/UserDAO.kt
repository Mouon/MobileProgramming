package com.example.foodjoa.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)

    @Query("Select userPassword from users where userId = :ID")
    fun getPasswordById(ID:String): String

    @Query("Select * from users where userId = :ID")
    fun findUserById(ID:String): List<User>

    @Query("UPDATE users SET userPassword = :newPassword WHERE userId = :id")
    fun updatePasswordById(id: String, newPassword: String)

    @Query("SELECT userNickname FROM users WHERE userId = :ID")
    fun getNicknameById(ID: String): String

    @Query("SELECT hour FROM users WHERE userId = :ID")
    fun getHour(ID: String) : Int

    @Query("SELECT minute FROM users WHERE userId = :ID")
    fun getMinute(ID: String) : Int

    @Query("SELECT checked FROM users WHERE userId = :ID")
    fun getIsChecked(ID: String) : Boolean

    @Query("UPDATE users SET hour = :newHour WHERE userId = :ID")
    fun updateHourById(ID: String, newHour: Int)

    @Query("UPDATE users SET minute = :newMinute WHERE userId = :ID")
    fun updateMinuteById(ID: String, newMinute: Int)

    @Query("UPDATE users SET checked = :newChecked WHERE userId = :ID")
    fun updateCheckedById(ID: String, newChecked: Boolean)
}