package com.example.foodjoa.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodjoa.database.AppDatabase
import com.example.foodjoa.database.User
import com.example.foodjoa.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity: AppCompatActivity() {
    var binding: ActivityRegisterBinding ?= null
    lateinit var appDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        register()
    }

    fun clearEditText(){
        binding!!.apply {
            etEmail.text.clear()
            etId.text.clear()
            etNick.text.clear()
            etPass.text.clear()
            etPassck.text.clear()
        }
    }


    suspend fun checkValidUser(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            appDb.UserDAO().findUserById(id).isNotEmpty()
        }
    }

    private fun register() {
        appDb = AppDatabase.getAppDatabase(this)
        binding!!.apply {
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString()
                val id = etId.text.toString()
                val nickName = etNick.text.toString()
                val password = etPass.text.toString()
                val passwordCheck = etPassck.text.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    if (email.isBlank() || id.isBlank() || nickName.isBlank() || password.isBlank() || passwordCheck.isBlank()) {
                        clearEditText()
                        Toast.makeText(this@RegisterActivity, "빈칸을 모두 입력하세요.", Toast.LENGTH_SHORT).show()
                    }
                    else if (email.contains("@")){
                        val mailName = email.split("@")
                        if (mailName[1].contains(".")) {
                            if (checkValidUser(id)) {
                                clearEditText()
                                Toast.makeText(this@RegisterActivity, "이미 등록된 사용자 입니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                if (password == passwordCheck) {
                                    val user = User(0,email,id,nickName,password,9,0,false)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        appDb.UserDAO().insertUser(user)
                                    }
                                    Toast.makeText(this@RegisterActivity, "회원가입 완료!.", Toast.LENGTH_SHORT).show()

                                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    this@RegisterActivity.finish()
                                    startActivity(intent)

                                } else {
                                    clearEditText()
                                    Toast.makeText(this@RegisterActivity, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else {
                            clearEditText()
                            Toast.makeText(this@RegisterActivity, "이메일 형식을 확인하세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        clearEditText()
                        Toast.makeText(this@RegisterActivity, "이메일 형식을 확인하세요.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}