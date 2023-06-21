package com.example.foodjoa.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodjoa.database.JANG
import com.example.foodjoa.adapters.MyInsertItemRecyclerViewAdapter
import com.example.foodjoa.R
import com.example.foodjoa.databinding.FragmentItemInsert2Binding

class Item_Insert_Fragment2 : Fragment() {

    var binding : FragmentItemInsert2Binding?= null
    var adapter : MyInsertItemRecyclerViewAdapter?= null
    lateinit var jang : JANG
    lateinit var newjang : JANG
    var itemList = ArrayList<JANG>()

    lateinit var category : String
    val meatList = arrayListOf<String>("소고기", "돼지고기", "닭고기", "육류기타")
    val seafoodList = arrayListOf<String>("어패류", "갑각류", "생선", "생션류기타")
    val fruitvegList = arrayListOf<String>("과일", "열매채소", "줄기채소", "뿌리채소", "과일/야채류기타")
    val snackList = arrayListOf<String>("과자", "아이스크림", "과자/빙과류기타")
    val frozenList = arrayListOf<String>("만두류", "냉동면류", "가공육류", "냉동식품기타")
    val drinkList = arrayListOf<String>("물", "주스", "우유","술", "음료류기타")
    val outeatList = arrayListOf<String>("배달", "외식", "외식기타")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        jang = arguments?.getSerializable("newJANG2") as JANG
        itemList = arguments?.getSerializable(("janglist2")) as ArrayList<JANG>
        category = arguments?.getString("Category").toString()

        binding = FragmentItemInsert2Binding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.itemrecyclerview.layoutManager =
            LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)

        // binding!!.recyclerView. addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        when(category){
            "육류" -> adapter = MyInsertItemRecyclerViewAdapter(meatList)
            "해산물" -> adapter = MyInsertItemRecyclerViewAdapter(seafoodList)
            "과일/야채류" -> adapter = MyInsertItemRecyclerViewAdapter(fruitvegList)
            "과자/빙과류" -> adapter = MyInsertItemRecyclerViewAdapter(snackList)
            "냉동식품" -> adapter = MyInsertItemRecyclerViewAdapter(frozenList)
            "음료류" -> adapter = MyInsertItemRecyclerViewAdapter(drinkList)
            "외식" -> adapter = MyInsertItemRecyclerViewAdapter(outeatList)
        }

        adapter?.itemClickListener = object : MyInsertItemRecyclerViewAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int, items: ArrayList<String>) {
                if (category == "외식") {
                    val fragment = requireActivity().supportFragmentManager.beginTransaction()
                    val bundle = Bundle()
                    val InsertFragment4 = Item_Insert_Fragment_Eatout()
                    // fragment.addToBackStack(null)
                    fragment.add(R.id.main_frm, InsertFragment4)
                    bundle.putString("Name", items[position])
                    newjang = JANG(
                        0,
                        jang.category,
                        items[position],
                        0,
                        0,
                        0,
                        jang.year,
                        jang.month,
                        jang.day,
                        jang.week,
                        jang.weekend
                    )

                    bundle.putSerializable("newJANG3", newjang)
                    bundle.putSerializable("janglist3", itemList)
                    InsertFragment4.arguments = bundle

                    fragment.show(InsertFragment4)
                    fragment.hide(this@Item_Insert_Fragment2)
                    //fragment.replace(R.id.itemframeLayout, InsertFragment3)
                    fragment.commit()
                } else {
                    val fragment = requireActivity().supportFragmentManager.beginTransaction()
                    val bundle = Bundle()
                    val InsertFragment3 = Item_Insert_Fragment3()
                    // fragment.addToBackStack(null)
                    fragment.add(R.id.main_frm, InsertFragment3)
                    bundle.putString("Name", items[position])
                    newjang = JANG(
                        0,
                        jang.category,
                        items[position],
                        0,
                        0,
                        0,
                        jang.year,
                        jang.month,
                        jang.day,
                        jang.week,
                        jang.weekend
                    )

                    bundle.putSerializable("newJANG3", newjang)
                    bundle.putSerializable("janglist3", itemList)
                    InsertFragment3.arguments = bundle

                    fragment.show(InsertFragment3)
                    fragment.hide(this@Item_Insert_Fragment2)
                    //fragment.replace(R.id.itemframeLayout, InsertFragment3)
                    fragment.commit()
                }
            }
        }


        binding!!.itemrecyclerview.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding!!.itemrecyclerview.adapter = null
        adapter!!.items.clear()
        adapter = null
        binding = null

    }
}