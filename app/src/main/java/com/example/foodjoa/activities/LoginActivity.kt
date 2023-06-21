// LoginActivity.kt
package com.example.foodjoa.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodjoa.database.AppDatabase
import com.example.foodjoa.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginInit()
    }

    private fun startMainActivity(userId: String, userNickname: String?) {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user_id", userId)
        intent.putExtra("user_nickname", userNickname) // 닉네임 정보도 전달
        startActivity(intent)
        finish()
    }

    private fun loginInit() {
        userDb = AppDatabase.getAppDatabase(this)

        binding.apply {
            btnLogin.setOnClickListener {
                val id = etIdLogin.text.toString()
                val password = etPassLogin.text.toString()

                if (id.isNotEmpty() && password.isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (checkValidId(id)) {
                            if (password == checkValidPassword(id)) {
                                Toast.makeText(this@LoginActivity, "로그인 완료", Toast.LENGTH_SHORT).show()
                                val userNickname = checkValidNickname(id)
                                startMainActivity(id, userNickname)
                            } else {
                                Toast.makeText(this@LoginActivity, "비밀번호 오류입니다.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "가입된 계정이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            btnRegisterLogin.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private suspend fun checkValidId(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            userDb.UserDAO().findUserById(id).isNotEmpty()
        }
    }

    private suspend fun checkValidPassword(id: String): String {
        return withContext(Dispatchers.IO) {
            userDb.UserDAO().getPasswordById(id)
        }
    }

    private suspend fun checkValidNickname(id: String): String? {
        return withContext(Dispatchers.IO) {
            userDb.UserDAO().getNicknameById(id)
        }
    }
}
