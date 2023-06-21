package com.example.foodjoa.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodjoa.database.AppDatabase
import com.example.foodjoa.database.UserDAO
import com.example.foodjoa.databinding.ActivityChangepasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangepasswordBinding
    private lateinit var userDb: AppDatabase
    private lateinit var userDAO: UserDAO
    private lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangepasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDb = AppDatabase.getAppDatabase(this)
        userDAO = userDb.UserDAO()

        CoroutineScope(Dispatchers.Main).launch {
            currentUserId = withContext(Dispatchers.IO) { getCurrentUserId() }

            binding.changePasswordButton.setOnClickListener {
                val currentPassword = binding.currentPasswordEditText.text.toString()
                val newPassword = binding.newPasswordEditText.text.toString()

                CoroutineScope(Dispatchers.Main).launch {
                    val currentUser = withContext(Dispatchers.IO) { userDAO.findUserById(currentUserId) }

                    if (currentUser.isNotEmpty()) {
                        val storedPassword = withContext(Dispatchers.IO) { checkValidPassword(currentUserId, currentPassword) }
                        if (storedPassword.isNotEmpty()) {
                            if (storedPassword == currentPassword) {
                                withContext(Dispatchers.IO) {
                                    updateUserPassword(currentUserId, newPassword)
                                }
                                Toast.makeText(this@ChangePasswordActivity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                                finish() // 비밀번호 변경 후 액티비티 종료
                            } else {
                                Toast.makeText(this@ChangePasswordActivity, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@ChangePasswordActivity, "사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ChangePasswordActivity, "사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private suspend fun getCurrentUserId(): String = withContext(Dispatchers.IO) {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        sharedPreferences.getString("userId", "") ?: ""
    }


    private suspend fun checkValidPassword(id: String, currentPassword: String): String {
        val storedPassword = withContext(Dispatchers.IO) { userDAO.getPasswordById(id) }
        return if (storedPassword == currentPassword) {
            storedPassword
        } else {
            ""
        }
    }


    private suspend fun updateUserPassword(id: String, newPassword: String) {
        userDAO.updatePasswordById(id, newPassword)
    }
}
