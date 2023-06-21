package com.example.foodjoa.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.foodjoa.R
import com.example.foodjoa.database.JANG
import com.example.foodjoa.databinding.FragmentItemInsert3Binding

class Item_Insert_Fragment3 : Fragment() {
    //val insertFragment = Item_Insert_Fragment3()

    var binding : FragmentItemInsert3Binding?= null
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
        binding = FragmentItemInsert3Binding.inflate(layoutInflater, container, false)
        adapter  = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            countdata
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apply {
            unitspinner.adapter = adapter
            unitspinner.setSelection(0)
            unitspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when(position){
                        0 -> {
                            binding!!.editCount.hint = "몇 개를 구매하시나요?"
                            costcount = 0
                        }
                        1 -> {
                            binding!!.editCount.hint = "몇 마리를 구매하시나요?"
                            costcount = 1
                        }
                        2 -> {
                            binding!!.editCount.hint = "몇 그램을 구매하시나요?"
                            costcount = 2
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
            binding!!.editCount.addTextChangedListener(
                afterTextChanged ={
                    val str = it.toString()
                    binding!!.insertbtn.isEnabled = str.isNotEmpty()
                }
            )
            binding!!.editCost.addTextChangedListener(
                afterTextChanged ={
                    val str = it.toString()
                    binding!!.insertbtn.isEnabled = str.isNotEmpty()
                }
            )

            binding!!.insertbtn.setOnClickListener {
                if(editCost.text.isNotEmpty() && editCount.text.isNotEmpty()) {

                    val fragment = requireActivity().supportFragmentManager.beginTransaction()
                    val itemContentFragment = Item_Content_Fragment()
                    val bundle = Bundle()

                    newjang = JANG(
                        0,
                        jang.category,
                        jang.productname,
                        binding!!.editCount.text.toString().toInt(),
                        costcount,
                        binding!!.editCost.text.toString().toInt(),
                        jang.year, jang.month, jang.day,jang.week,
                        jang.weekend
                    )
                    
                    editCount.text.clear()
                    editCost.text.clear()
                    bundle.putString("date","${jang.year}년${jang.month}월${jang.day}일")
                    bundle.putInt("week", jang.week)

                    bundle.putSerializable("JANG", newjang)
                    bundle.putSerializable("janglist", itemList)
                    itemContentFragment.arguments = bundle
                    fragment.add(R.id.main_frm ,itemContentFragment)
                    fragment.show(itemContentFragment)
                    fragment.hide(this@Item_Insert_Fragment3)
                    //fragment.replace(R.id.itemframeLayout,itemContentFragment)
                    fragment.commit()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding!!.unitspinner.adapter = null
        adapter = null
        binding = null
    }
}