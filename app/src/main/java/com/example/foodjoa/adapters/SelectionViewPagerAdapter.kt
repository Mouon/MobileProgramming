// 여기는 viewPager 전용 adapter (앱 실행시 나오는 슬라이드 프레그먼트 슬라이드로 넘기기 용)

package com.example.foodjoa.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodjoa.fragments.StartFragment1
import com.example.foodjoa.fragments.StartFragment2
import com.example.foodjoa.fragments.StartFragment3
import com.example.foodjoa.fragments.StartFragment4

class SelectionViewPagerAdapter(fragmentActivity: FragmentActivity) :  FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return StartFragment1()
            1 -> return StartFragment2()
            2 -> return StartFragment3()
            3 -> return StartFragment4()
            else -> return StartFragment1()
        }
    }

}
