package com.example.foodjoa.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodjoa.R

class ItemFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item_content = ItemFirstFragment()
        val fragment = requireActivity().supportFragmentManager.beginTransaction()
        fragment.add(R.id.main_frm, item_content)
        fragment.show(item_content)
        fragment.hide(this@ItemFragment)
        //fragment.replace(R.id.main_frm, item_content)
        fragment.commit()
    }
}