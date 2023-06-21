package com.example.foodjoa.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodjoa.activities.LoginActivity
import com.example.foodjoa.activities.RegisterActivity
import com.example.foodjoa.databinding.FragmentStart4Binding

class StartFragment4 : Fragment() {
    var binding : FragmentStart4Binding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStart4Binding.inflate(layoutInflater, container, false)
        return binding!!.root
    }


    //
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            //여기 클릭 리스너를 통해 로그인, 회원가입 화면 이동
            signinBtn.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                requireActivity().finish()
                startActivity(intent)


            }
            signupBtn.setOnClickListener {
                val intent = Intent(requireActivity(), RegisterActivity::class.java)
                requireActivity().finish()
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}