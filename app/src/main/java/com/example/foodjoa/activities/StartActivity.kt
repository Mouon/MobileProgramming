// 앱을 실행후 시작화면

package com.example.foodjoa.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodjoa.adapters.SelectionViewPagerAdapter
import com.example.foodjoa.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    var binding : ActivityStartBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initLayout()
    }

    private fun initLayout() {
        binding!!.viewpager.adapter = SelectionViewPagerAdapter(this)
        binding!!.indicator.setViewPager(binding!!.viewpager)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding!!.viewpager.adapter = null
        binding = null
    }


}