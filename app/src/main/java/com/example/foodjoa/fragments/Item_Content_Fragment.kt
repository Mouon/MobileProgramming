package com.example.foodjoa.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodjoa.database.JANG
import com.example.foodjoa.adapters.MyItemRecyclerViewAdapter
import com.example.foodjoa.R
import com.example.foodjoa.database.AppDatabase
import com.example.foodjoa.databinding.FragmentItemContentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Item_Content_Fragment : Fragment() {
    private val TAG = javaClass.simpleName
    lateinit var appDb: AppDatabase
    lateinit var newjang : JANG
    var jang : JANG?= null
    lateinit var adapter : MyItemRecyclerViewAdapter
    var binding : FragmentItemContentBinding?= null
    var itemList = ArrayList<JANG>()
    var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentItemContentBinding.inflate(layoutInflater, container, false)
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerView. addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayout.VERTICAL
            )
        )
        jang = arguments?.getSerializable("JANG") as? JANG
        itemList = arguments?.getSerializable(("janglist")) as? ArrayList<JANG> ?: ArrayList()
        //  if (jang != null) {
        //     adapter.addItem(jang!!)
        //  }
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appDb = AppDatabase.getAppDatabase(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedDate = arguments?.getString("date")!!
        val week = arguments?.getInt("week", -1)!!
        val date = extractDate(selectedDate)
        val weekend = arguments?.getInt("weekend", -1)!!

        val (year, month, day) = date!!

        adapter = MyItemRecyclerViewAdapter(itemList)
        if (jang!=null) {

            itemList.add(jang!!)
            adapter = MyItemRecyclerViewAdapter(itemList)
            adapter.notifyDataSetChanged()
        }

        binding!!.recyclerView.adapter = adapter


        binding!!.apply {
            InsertItemContentbtn.setOnClickListener {
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val bundle = Bundle()
                val insert1Fragment = Item_Insert_Fragment1()
                if (adapter.items.isEmpty()) {
                    newjang = JANG(0, "","",0,0,0,year,month,day,week,changeday(weekend))
                }
                else {
                    newjang = JANG(0, "","",0,0,0,jang!!.year,jang!!.month,jang!!.day,jang!!.week, jang!!.weekend)
                }
                bundle.putSerializable("newJANG1",newjang)
                bundle.putParcelableArrayList("janglist1", adapter.items)
                insert1Fragment.arguments = bundle
                fragment.add(R.id.main_frm, insert1Fragment)
                //fragment.add(R.id.itemframeLayout, insertFragment)
                fragment.show(insert1Fragment)
                fragment.hide(this@Item_Content_Fragment)
                //fragment.replace(R.id.itemframeLayout, insertFragment)
                fragment.commit()
            }

            ItemContentCanceltbtn.setOnClickListener {
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val calendarViewFragment = ItemFragment()
                fragment.add(R.id.main_frm, calendarViewFragment)
                fragment.show(calendarViewFragment)
                fragment.remove(this@Item_Content_Fragment)
                fragment.commit()
            }
            ItemContentChecktbtn.setOnClickListener {
                //여ㅑ기에 쿼리문 쓰기
                if(adapter.items.isEmpty()) {

                }
                else {
                    CoroutineScope(Dispatchers.IO).launch {
                        for (data in adapter.items) {
                            appDb.JangDAO().insertItem(data)
                            Log.d(TAG, "$data")
                        }
                    }
                    val fragment = requireActivity().supportFragmentManager.beginTransaction()
                    val calendarViewFragment = ItemFragment()
                    fragment.add(R.id.main_frm, calendarViewFragment)
                    fragment.show(calendarViewFragment)
                    fragment.remove(this@Item_Content_Fragment)
                    fragment.commit()
                }

            }
        }

    }

    fun changeday(day : Int) : String{
        when (day) {
            0 -> return "일"
            1 -> return "월"
            2 -> return "화"
            3 -> return "수"
            4 -> return "목"
            5 -> return "금"
            6 -> return "토"
            else -> return "오류"
        }

    }

    private fun extractDate(str: String): Triple<Int, Int, Int>? {
        val pattern = """(\d{4})년(\d{1,2})월(\d{1,2})일""".toRegex()
        val matchResult = pattern.find(str)
        if (matchResult != null) {
            val (year, month, day) = matchResult.destructured
            return Triple(year.toInt(), month.toInt(), day.toInt())
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        binding!!.recyclerView.adapter = null
        binding =null
    }
}