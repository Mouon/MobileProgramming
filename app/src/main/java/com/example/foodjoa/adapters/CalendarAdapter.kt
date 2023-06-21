//달력용 adapter 입니다

package com.example.foodjoa.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.foodjoa.CustomCalendar
import com.example.foodjoa.R
import com.example.foodjoa.databinding.ListItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(
    val context: Context,
    val calendarLayout: LinearLayout,
    val date: Date
) : RecyclerView.Adapter<CalendarAdapter.CalendarItemHolder>() {

    private val TAG = javaClass.simpleName
    var dataList: ArrayList<Int> = arrayListOf()

    var customCalendar: CustomCalendar = CustomCalendar(date)

    init {
        customCalendar.initBaseCalendar()
        dataList = customCalendar.dateList
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemHolder {
        val binding = ListItemCalendarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarItemHolder, position: Int) {
        val h = calendarLayout.height / 6
        holder.itemView.layoutParams.height = h

        holder.bind(dataList[position], position)
        if (itemClick != null) {
            holder.itemView.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class CalendarItemHolder(private val binding: ListItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Int, position: Int) {
            val firstDateIndex = customCalendar.prevTail
            val lastDateIndex = dataList.size - customCalendar.nextHead - 1
            binding.itemCalendarDateText.text = data.toString()

            val dateString: String = SimpleDateFormat("dd", Locale.KOREA).format(date)
            val dateInt = dateString.toInt()
            if (dataList[position] == dateInt) {
                binding.itemCalendarDateText.setTypeface(null, Typeface.BOLD)
            }

            if (position < firstDateIndex || position > lastDateIndex) {
                binding.itemCalendarDateText.setTextAppearance(R.style.LightColorTextViewStyle)
                binding.itemCalendarDotView.background = null
            }
        }
    }
}