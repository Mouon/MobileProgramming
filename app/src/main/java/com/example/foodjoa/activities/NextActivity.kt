// 여기가 로그인 시 진입하는 activity 현준님께서 만드신걸로 대체하시면 됩니다.

package com.example.foodjoa.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.foodjoa.adapters.MainSelectionViewPagerAdapter
import com.example.foodjoa.R
import com.example.foodjoa.database.AppDatabase
import com.example.foodjoa.database.UserDAO
import com.example.foodjoa.databinding.ActivityNextBinding
import com.example.foodjoa.fragments.SettingsFragment
import com.google.android.material.tabs.TabLayoutMediator

class NextActivity : AppCompatActivity() {
    private lateinit var userDatabase: AppDatabase
    var binding: ActivityNextBinding ?= null
    val textarr = arrayListOf<String>("Home", "Cost", "Item", "Settings")
    var imgArr = arrayListOf<Int>(
        R.drawable.baseline_home_24, R.drawable.baseline_insert_chart_outlined_24,
        R.drawable.baseline_list_alt_24, R.drawable.baseline_settings_24
    )


    private fun startMainActivity(userId: String, userNickname: String?) {
        // ...
        val intent = Intent(this, NextActivity::class.java)
        intent.putExtra("user_id", userId)
        userNickname?.let {
            intent.putExtra("user_nickname", it)
        }
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextBinding.inflate(LayoutInflater.from(this))
        setContentView(binding!!.root)

        userDatabase = AppDatabase.getAppDatabase(this)

        val userId = intent.getStringExtra("user_id")
        val userNickname = intent.getStringExtra("user_nickname")

        if (userId != null && userNickname != null) {
            val settingsFragment = SettingsFragment.newInstance(userId, userNickname)
            if (userId != null && userNickname != null) {
                val settingsFragment = SettingsFragment.newInstance(userId, userNickname)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, settingsFragment)
                    commit()
                }

            }

        }

        init()
    }

    fun getUserDAO(): UserDAO {
        return userDatabase.UserDAO()
    }

    fun init(){
        binding!!.viewpager.adapter = MainSelectionViewPagerAdapter(this)
        TabLayoutMediator(binding!!.tablayout, binding!!.viewpager) {
                tab, pos ->
            tab.text = textarr[pos]
            tab.setIcon(imgArr[pos])
        }.attach()



    }

    override fun onDestroy() {
        super.onDestroy()

        binding!!.viewpager.adapter = null
        binding = null
    }
}