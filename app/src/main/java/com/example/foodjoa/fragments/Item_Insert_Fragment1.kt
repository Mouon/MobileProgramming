package com.example.foodjoa.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodjoa.Category
import com.example.foodjoa.database.JANG
import com.example.foodjoa.R
import com.example.foodjoa.databinding.FragmentItemInsert1Binding

class Item_Insert_Fragment1 : Fragment() {
    var binding : FragmentItemInsert1Binding?= null
    lateinit var newjang : JANG
    var itemList = ArrayList<JANG>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        newjang = arguments?.getSerializable("newJANG1") as JANG
        itemList = arguments?.getSerializable(("janglist1")) as? ArrayList<JANG> ?: ArrayList()
        binding = FragmentItemInsert1Binding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val insertitemfragment2 = Item_Insert_Fragment2()
        binding!!.apply {
            itemMeat.setOnClickListener {
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val bundle = Bundle()

                //  fragment.addToBackStack(null)
                fragment.add(R.id.main_frm, insertitemfragment2)
                bundle.putString("Category", Category.MEAT.indegredients)
                newjang = JANG(0,Category.MEAT.indegredients, "",0,0,0,newjang.year, newjang.month, newjang.day, newjang.week, newjang.weekend)
                bundle.putSerializable("newJANG2", newjang)
                bundle.putSerializable("janglist2", itemList)
                insertitemfragment2.arguments = bundle
                fragment.show(insertitemfragment2)
                fragment.hide(this@Item_Insert_Fragment1)
                fragment.commit()
            }
            itemSeafood.setOnClickListener {
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val bundle = Bundle()
                // fragment.addToBackStack(null)
                fragment.add(R.id.main_frm, insertitemfragment2)
                bundle.putString("Category", Category.SEAFOOD.indegredients)
                newjang = JANG(0,Category.SEAFOOD.indegredients, "",0,0,0,newjang.year, newjang.month, newjang.day, newjang.week, newjang.weekend)
                bundle.putSerializable("newJANG2", newjang)
                bundle.putSerializable("janglist2", itemList)
                insertitemfragment2.arguments = bundle
                fragment.show(insertitemfragment2)
                fragment.hide(this@Item_Insert_Fragment1)
                fragment.commit()
            }
            itemFruitvegi.setOnClickListener {
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val bundle = Bundle()

                //fragment.addToBackStack(null)
                fragment.add(R.id.main_frm, insertitemfragment2)

                bundle.putString("Category", Category.FRUITVEG.indegredients)
                newjang = JANG(0,Category.FRUITVEG.indegredients, "",0,0,0,newjang.year, newjang.month, newjang.day, newjang.week, newjang.weekend)
                bundle.putSerializable("newJANG2", newjang)
                bundle.putSerializable("janglist2", itemList)
                insertitemfragment2.arguments = bundle
                fragment.show(insertitemfragment2)
                fragment.hide(this@Item_Insert_Fragment1)
                fragment.commit()
            }
            itemSnack.setOnClickListener {
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val bundle = Bundle()
                //   fragment.addToBackStack(null)
                fragment.add(R.id.main_frm, insertitemfragment2)

                bundle.putString("Category", Category.SNACK.indegredients)
                newjang = JANG(0,Category.SNACK.indegredients, "",0,0,0,newjang.year, newjang.month, newjang.day, newjang.week, newjang.weekend)
                bundle.putSerializable("newJANG2", newjang)
                bundle.putSerializable("janglist2", itemList)
                insertitemfragment2.arguments = bundle
                fragment.show(insertitemfragment2)
                fragment.hide(this@Item_Insert_Fragment1)
                fragment.commit()

            }
            itemFrozenfood.setOnClickListener {
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val bundle = Bundle()
                //  fragment.addToBackStack(null)
                fragment.add(R.id.main_frm, insertitemfragment2)

                bundle.putString("Category", Category.FROZENFOOD.indegredients.toString())
                newjang = JANG(0,Category.FROZENFOOD.indegredients, "",0,0,0,newjang.year, newjang.month, newjang.day, newjang.week, newjang.weekend)
                bundle.putSerializable("newJANG2", newjang)
                bundle.putSerializable("janglist2", itemList)
                insertitemfragment2.arguments = bundle
                fragment.show(insertitemfragment2)
                fragment.hide(this@Item_Insert_Fragment1)
                fragment.commit()
            }
            itemDrink.setOnClickListener {
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val bundle = Bundle()

                //  fragment.addToBackStack(null)
                fragment.add(R.id.main_frm, insertitemfragment2)

                bundle.putString("Category", Category.DRINK.indegredients.toString())
                newjang = JANG(0,Category.DRINK.indegredients, "",0,0,0,newjang.year, newjang.month, newjang.day, newjang.week, newjang.weekend)
                bundle.putSerializable("newJANG2", newjang)
                bundle.putSerializable("janglist2", itemList)
                insertitemfragment2.arguments = bundle
                fragment.show(insertitemfragment2)
                fragment.hide(this@Item_Insert_Fragment1)
                fragment.commit()
            }
            itemEatout.setOnClickListener {
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val bundle = Bundle()

                // fragment.addToBackStack(null)
                fragment.add(R.id.main_frm, insertitemfragment2)

                bundle.putString("Category", Category.EATOUT.indegredients.toString())
                newjang = JANG(0,Category.EATOUT.indegredients, "",0,0,0,newjang.year, newjang.month, newjang.day, newjang.week, newjang.weekend)
                bundle.putSerializable("newJANG2", newjang)
                bundle.putSerializable("janglist2", itemList)
                insertitemfragment2.arguments = bundle
                fragment.show(insertitemfragment2)
                fragment.hide(this@Item_Insert_Fragment1)
                fragment.commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}