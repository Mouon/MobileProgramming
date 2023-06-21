package com.example.foodjoa.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.foodjoa.R
import com.example.foodjoa.database.JANG
import com.example.foodjoa.databinding.FragmentItemInsertEatoutBinding

class Item_Insert_Fragment_Eatout : Fragment() {

    var binding : FragmentItemInsertEatoutBinding?= null
    var adapter : ArrayAdapter<String>?= null
    lateinit var jang : JANG
    lateinit var newjang : JANG
    var itemList = ArrayList<JANG>()
    var costcount : Int = 0
    val countdata = arrayOf("개", "마리", "그램")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jang = arguments?.getSerializable("newJANG3") as JANG
        itemList = arguments?.getSerializable(("janglist3")) as ArrayList<JANG>
        binding = FragmentItemInsertEatoutBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apply {



        binding!!.editCost.addTextChangedListener(
            afterTextChanged ={
                val str = it.toString()
                binding!!.insertbtn.isEnabled = str.isNotEmpty()
            }
        )

            binding!!.insertbtn.setOnClickListener {
                if(editCost.text.isNotEmpty()) {

                    val fragment = requireActivity().supportFragmentManager.beginTransaction()
                    val itemContentFragment = Item_Content_Fragment()
                    val bundle = Bundle()

                    newjang = JANG(
                        0,
                        jang.category,
                        jang.productname,
                        0,
                        -1,
                        binding!!.editCost.text.toString().toInt(),
                        jang.year, jang.month, jang.day,jang.week,
                        jang.weekend
                    )
                    editCost.text.clear()
                    bundle.putString("date","${jang.year}년${jang.month}월${jang.day}일")
                    bundle.putInt("week", jang.week)

                    bundle.putSerializable("JANG", newjang)
                    bundle.putSerializable("janglist", itemList)
                    itemContentFragment.arguments = bundle
                    fragment.add(R.id.main_frm ,itemContentFragment)
                    fragment.show(itemContentFragment)
                    fragment.hide(this@Item_Insert_Fragment_Eatout)
                    //fragment.replace(R.id.itemframeLayout,itemContentFragment)
                    fragment.commit()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        adapter = null
        binding = null
    }
}