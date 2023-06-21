package com.example.foodjoa.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodjoa.adapters.CalendarAdapter
import com.example.foodjoa.R
import com.example.foodjoa.activities.MainActivity
import com.example.foodjoa.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment(index: Int) : Fragment() {
    var binding : FragmentCalendarBinding?= null
    private val TAG = javaClass.simpleName
    lateinit var mContext: Context
    lateinit var mActivity: MainActivity

    var pageIndex = index
    lateinit var currentDate: Date

    lateinit var calendar_year_month_text: TextView
    lateinit var calendar_layout: LinearLayout
    lateinit var calendar_view: RecyclerView
    lateinit var calendarAdapter: CalendarAdapter

    companion object {
        var instance: CalendarFragment? = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        ////val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)

        mContext = context as MainActivity
//            mActivity = activity as MainActivity

        initView()
        initCalendar()

        //return view
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun initView() {
        pageIndex -= Int.MAX_VALUE/2
        Log.d(TAG, "흠")
        Log.d(TAG, "$pageIndex")
        calendar_year_month_text = binding!!.calendarYearMonthText
        calendar_layout = binding!!.calendarLayout
        calendar_view = binding!!.calendarView
        val date = Calendar.getInstance().run {
            add(Calendar.MONTH, pageIndex)
            time
        }
        currentDate = date
        var datetime: String = SimpleDateFormat(
            mContext.getString(R.string.calendar_year_month_format),
            Locale.KOREA
        ).format(date.time)
        calendar_year_month_text.setText(datetime)
    }

    fun initCalendar() {
        // 각 월의 1일의 요일을 구해
        // 첫 주의 일요일~해당 요일 전까지는 ""으로
        // 말일까지 해당 날짜
        // 마지막 날짜 뒤로는 ""으로 처리하여
        // CalendarAdapter로 List를 넘김
        calendarAdapter = CalendarAdapter(mContext, calendar_layout, currentDate)
        calendar_view.adapter = calendarAdapter
        calendar_view.layoutManager =
            GridLayoutManager(mContext, 7, GridLayoutManager.VERTICAL, false)
        calendar_view.setHasFixedSize(true)
        calendarAdapter.itemClick = object :
            CalendarAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val firstDateIndex = calendarAdapter.dataList.indexOf(1)
                val lastDateIndex = calendarAdapter.dataList.lastIndexOf(calendarAdapter.customCalendar.currentMaxDate)
                // 현재 월의 1일 이전, 현재 월의 마지막일 이후는 터치 disable
                if (position < firstDateIndex || position > lastDateIndex) {
                    return
                }
                val day = calendarAdapter.dataList[position].toString()
                val show_date = "${calendar_year_month_text.text}${day}일"
                //val mainTab = mActivity.main_bottom_menu
                //mainTab.setScrollPosition(1, 0f, true)
                OnItemClick(show_date, position)
            }
        }
    }
    fun OnItemClick(date:String, week:Int){

        val item_content = ItemCalenderViewFragment()
        val fragment = requireActivity().supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("date", date)
        bundle.putInt("week", week/7+1)
        bundle.putInt("weekend", week%7)
        //fragment.add(R.id.itemframeLayout, item_content)
        item_content.arguments = bundle
        fragment.add(R.id.main_frm, item_content)
        //fragment.show(item_content)
        //fragment.hide(this@CalendarFragment)
        fragment.replace(R.id.main_frm, item_content)
        fragment.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}