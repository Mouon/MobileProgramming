package com.example.foodjoa.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodjoa.R
import com.example.foodjoa.databinding.FragmentCostBinding

class CostFragment : Fragment() {
    private lateinit var binding: FragmentCostBinding
    private var currentFragment: Fragment? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCostBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchFragment(CostFragmentByMonth())
        binding.button1.setOnClickListener {
            switchFragment(CostFragmentByMonth())
        }

        binding.button2.setOnClickListener {
            switchFragment(CostFragmentByWeek())
        }
    }

    private fun switchFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().apply {
            currentFragment?.let { remove(it) }
            currentFragment = fragment
            add(R.id.fragmentContainer, fragment)
            commit()
        }
    }

}