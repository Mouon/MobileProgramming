// main화면에서 탭바 전용 viewpager인데 현준님께서 만드신 걸로 대체 해주시면 됩니다

package com.example.foodjoa.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodjoa.fragments.CostFragment
import com.example.foodjoa.fragments.HomeFragment
import com.example.foodjoa.fragments.ItemFragment
import com.example.foodjoa.fragments.SettingsFragment

class MainSelectionViewPagerAdapter(fragmentActivity: FragmentActivity) :  FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return HomeFragment()
            1 -> return CostFragment()
            2 -> return ItemFragment()
            3 -> return SettingsFragment()
            else -> return HomeFragment()
        }
    }
}