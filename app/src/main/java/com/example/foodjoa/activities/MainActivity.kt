package com.example.foodjoa.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodjoa.R
import com.example.foodjoa.databinding.ActivityMainBinding
import com.example.foodjoa.fragments.CostFragment
import com.example.foodjoa.fragments.HomeFragment
import com.example.foodjoa.fragments.ItemFragment
import com.example.foodjoa.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var exit_millis :Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainBnv.visibility = View.VISIBLE
        binding.mainBnv.selectedItemId = R.id.homeFragment
        initBottomNavigation()
    }
    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        val bnv_fvbyId = findViewById<BottomNavigationView>(R.id.main_bnv)
        bnv_fvbyId.setOnClickListener { }

        binding.mainBnv.setOnClickListener { }

        binding.mainBnv.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.itemFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ItemFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.costFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, CostFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.settingFragment -> {
                    val userId = intent.getStringExtra("user_id") // MainActivity에서 전달된 userId 값을 가져옴
                    val userNickname = intent.getStringExtra("user_nickname") // MainActivity에서 전달된 userNickname 값을 가져옴
                    val settingsFragment = SettingsFragment.newInstance(userId!!, userNickname!!)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, settingsFragment)
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

        binding.mainBnv.itemIconTintList = null

        // ...
    }
    fun killApp() {
        if(System.currentTimeMillis() - exit_millis > 2000){
            exit_millis=System.currentTimeMillis()
            Toast.makeText( this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }else{
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Main_lifecycle", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Main_lifecycle", "onResume()")
    }
    override fun onPause() {
        super.onPause()
        Log.d("Main_lifecycle", "onPause()")
    }
    override fun onStop() {
        super.onStop()
        Log.d("Main_lifecycle", "onStop()")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("Main_lifecycle", "onDestroy()")
    }

}