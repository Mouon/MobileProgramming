package com.example.foodjoa.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodjoa.Category
import com.example.foodjoa.adapters.ItemExpandableAdapter
import com.example.foodjoa.R
import com.example.foodjoa.database.AppDatabase
import com.example.foodjoa.database.JANG
import com.example.foodjoa.databinding.FragmentItemCalenderViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemCalenderViewFragment : Fragment() {
    var binding: FragmentItemCalenderViewBinding?= null
    var adapter_meat = ItemExpandableAdapter(ArrayList<JANG>())
    var adapter_seafood = ItemExpandableAdapter(ArrayList<JANG>())
    var adapter_fruitveg = ItemExpandableAdapter(ArrayList<JANG>())
    var adapter_snack = ItemExpandableAdapter(ArrayList<JANG>())
    var adapter_frozen = ItemExpandableAdapter(ArrayList<JANG>())
    var adapter_drink  = ItemExpandableAdapter(ArrayList<JANG>())
    var adapter_eatout = ItemExpandableAdapter(ArrayList<JANG>())

    lateinit var appDb: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentItemCalenderViewBinding.inflate(layoutInflater, container, false)

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
        val day = arguments?.getInt("weekend", -1)!!
        val date = extractDate(selectedDate)
        val (year, month, day_int) = date!!
        initRecyclerView(year,month,day_int)

        // 선택한 날짜를 사용하여 필요한 작업 수행
        binding!!.SelectedDate.text=selectedDate
        binding!!.apply {
            fabbtn.setOnClickListener {
                val item_content = Item_Content_Fragment()
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                val bundle = Bundle()
                bundle.putString("date", selectedDate)
                bundle.putInt("week", week)
                bundle.putInt("weekend", day)
                item_content.arguments = bundle
                fragment.add(R.id.main_frm, item_content)
                fragment.show(item_content)
                fragment.hide(this@ItemCalenderViewFragment)
                //fragment.replace(R.id.itemframeLayout, item_content)
                fragment.commit()
            }
        }

        adapter_meat.itemClickListener = object : ItemExpandableAdapter.OnItemClickListener {
            override fun OnItemClick(data: JANG, position: Int) {
                callAlertDlg(data.itemId, month, day_int)
            }
        }
        adapter_seafood.itemClickListener = object : ItemExpandableAdapter.OnItemClickListener {
            override fun OnItemClick(data: JANG, position: Int) {
                callAlertDlg(data.itemId, month, day_int)
            }
        }
        adapter_fruitveg.itemClickListener = object : ItemExpandableAdapter.OnItemClickListener {
            override fun OnItemClick(data: JANG, position: Int) {
                callAlertDlg(data.itemId, month, day_int)
            }
        }
        adapter_snack.itemClickListener = object : ItemExpandableAdapter.OnItemClickListener {
            override fun OnItemClick(data: JANG, position: Int) {
                callAlertDlg(data.itemId, month, day_int)
            }
        }
        adapter_drink.itemClickListener = object : ItemExpandableAdapter.OnItemClickListener {
            override fun OnItemClick(data: JANG, position: Int) {
                callAlertDlg(data.itemId, month, day_int)
            }
        }
        adapter_frozen.itemClickListener = object : ItemExpandableAdapter.OnItemClickListener {
            override fun OnItemClick(data: JANG, position: Int) {
                callAlertDlg(data.itemId, month, day_int)
            }
        }
        adapter_eatout.itemClickListener = object : ItemExpandableAdapter.OnItemClickListener {
            override fun OnItemClick(data: JANG, position: Int) {
                callAlertDlg(data.itemId, month, day_int)
            }
        }



    }

    fun initRecyclerView(year : Int, month: Int, day_int: Int) {
        binding!!.apply {
            recyclerMeat.layoutManager =  LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerSeafood.layoutManager =  LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerFruitveg.layoutManager =  LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerSnack.layoutManager =  LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerDrink.layoutManager =  LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerFrozenfood.layoutManager =  LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerOuteat.layoutManager =  LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding!!.recyclerMeat. addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            binding!!.recyclerSeafood. addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            binding!!.recyclerFruitveg. addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            binding!!.recyclerSnack. addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            binding!!.recyclerDrink. addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            binding!!.recyclerFrozenfood. addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            binding!!.recyclerOuteat. addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

            CoroutineScope(Dispatchers.IO).launch {
                adapter_meat.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.MEAT.indegredients) as ArrayList<JANG>
                adapter_seafood.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.SEAFOOD.indegredients) as ArrayList<JANG>
                adapter_fruitveg.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.FRUITVEG.indegredients) as ArrayList<JANG>
                adapter_snack.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.SNACK.indegredients) as ArrayList<JANG>
                adapter_drink.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.DRINK.indegredients) as ArrayList<JANG>
                adapter_frozen.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.FROZENFOOD.indegredients) as ArrayList<JANG>
                adapter_eatout.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.EATOUT.indegredients) as ArrayList<JANG>
            }

            recyclerMeat.adapter = adapter_meat
            recyclerSeafood.adapter = adapter_seafood
            recyclerFruitveg.adapter = adapter_fruitveg
            recyclerSnack.adapter = adapter_snack
            recyclerDrink.adapter = adapter_drink
            recyclerFrozenfood.adapter = adapter_frozen
            recyclerOuteat.adapter = adapter_eatout
        }

    }

    fun getnewDB(month: Int, day_int: Int) {
        adapter_meat.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.MEAT.indegredients) as ArrayList<JANG>
        adapter_seafood.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.SEAFOOD.indegredients) as ArrayList<JANG>
        adapter_fruitveg.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.FRUITVEG.indegredients) as ArrayList<JANG>
        adapter_snack.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.SNACK.indegredients) as ArrayList<JANG>
        adapter_drink.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.DRINK.indegredients) as ArrayList<JANG>
        adapter_frozen.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.FROZENFOOD.indegredients) as ArrayList<JANG>
        adapter_eatout.items = appDb.JangDAO().getItemByDayAndCategory(month, day_int, Category.EATOUT.indegredients) as ArrayList<JANG>
    }

    fun callAlertDlg(itemid : Int, month: Int, day_int: Int) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("정말로 지우시겠습니까?")
            .setTitle("데이터 지우기").setPositiveButton("OK") {
                    _,_
                ->
                CoroutineScope(Dispatchers.IO).launch {
                    appDb.JangDAO().delete(itemid)
                    getnewDB(month, day_int)
                    CoroutineScope(Dispatchers.Main).launch {
                        adapter_meat.notifyDataSetChanged()
                        adapter_seafood.notifyDataSetChanged()
                        adapter_fruitveg.notifyDataSetChanged()
                        adapter_snack.notifyDataSetChanged()
                        adapter_drink.notifyDataSetChanged()
                        adapter_frozen.notifyDataSetChanged()
                        adapter_eatout.notifyDataSetChanged()
                    }

                }

            }.setNegativeButton("Cancel") {
                    dlg,_-> dlg.dismiss()
            }
        val dlg = builder.create()
        dlg.show()
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
        binding!!.recyclerMeat.adapter = null
        binding!!.recyclerSeafood.adapter = null
        binding!!.recyclerFruitveg.adapter = null
        binding!!.recyclerSnack.adapter = null
        binding!!.recyclerDrink.adapter = null
        binding!!.recyclerFrozenfood.adapter = null
        binding!!.recyclerOuteat.adapter = null
        binding =null
    }

}