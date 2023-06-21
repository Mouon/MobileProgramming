package com.example.foodjoa.fragments

import android.R
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.foodjoa.Category
import com.example.foodjoa.database.AppDatabase
import com.example.foodjoa.databinding.FragmentCostByMonthBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class CostFragmentByWeek : Fragment() {

    // Binding 객체 생성
    var binding: FragmentCostByMonthBinding?=null

    // Database 객체 생성
    lateinit var appDb: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCostByMonthBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appDb = AppDatabase.getAppDatabase(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val month = Calendar.getInstance().get(Calendar.MONTH)+1
        val week = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)
        initChart(month, week) // 처음엔 현재 월의 데이터를 가져옴
        initSpinner()
    }

    private fun initSpinner() {
        setHasOptionsMenu(true)

        // 스피너 어댑터 설정
        val weeks = arrayOf("1주차 소비", "2주차 소비", "3주차 소비", "4주차 소비", "5주차 소비")
        val weekInt = arrayOf(1,2,3,4,5)
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, weeks)
        binding!!.monthSpinner.adapter = spinnerAdapter

        // 현재 주차를 기본값으로 설정
        val currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)
        val defaultPosition = weekInt.indexOf(currentWeek)
        binding!!.monthSpinner.setSelection(defaultPosition)

        // 스피너 선택 이벤트 리스너 설정
        binding!!.monthSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedWeek = weekInt[position]
                val month = Calendar.getInstance().get(Calendar.MONTH)+1
                initChart(month, selectedWeek) // 선택된 월에 대한 차트 업데이트
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않을 때가 뭐지 ???
            }
        }
    }




// ============================================================================================





    inner class MyXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("일","월","화","수","목","금","토")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }

    suspend fun initWeekPriceData(month: Int, week: Int): Float {
        val totalSum = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByMonthAndWeek(month,week)
        }
        return totalSum
    }

    suspend fun initPieChartData1(month: Int, week: Int): ArrayList<PieEntry> {

        val outExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeekAndCategory(month, week, Category.EATOUT.indegredients)
        }

        val foodExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeekByFood(month, week, Category.EATOUT.indegredients)
        }

        val entries1 = ArrayList<PieEntry>()
        entries1.add(PieEntry(outExpense, "외식류"))
        entries1.add(PieEntry(foodExpense, "식품류"))
        return entries1
    }

    suspend fun initPieChartData2(month: Int, week: Int): ArrayList<PieEntry> {
        val meatExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeekAndCategory(month, week, Category.MEAT.indegredients)
        }
        val fishExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeekAndCategory(month, week, Category.SEAFOOD.indegredients)
        }
        val fruitExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeekAndCategory(month, week, Category.FRUITVEG.indegredients)
        }
        val snackExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeekAndCategory(month, week, Category.SNACK.indegredients)
        }

        val frozenExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeekAndCategory(month, week, Category.FROZENFOOD.indegredients)
        }
        val drinkExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeekAndCategory(month, week, Category.DRINK.indegredients)
        }


        val entries2 = ArrayList<PieEntry>()
        entries2.add(PieEntry(meatExpense.toFloat(), "고기류"))
        entries2.add(PieEntry(fishExpense.toFloat(), "해산물류"))
        entries2.add(PieEntry(fruitExpense.toFloat(), "과일야채류"))
        entries2.add(PieEntry(snackExpense.toFloat(), "과자/빙과류"))
        entries2.add(PieEntry(frozenExpense.toFloat(), "냉동식품류"))
        entries2.add(PieEntry(drinkExpense.toFloat(), "음료류"))

        return entries2
    }

    suspend fun initBarChartData1(month: Int, week: Int): ArrayList<BarEntry> {

        val weekFood1 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByFoodWeekend(month, week, Category.EATOUT.indegredients, "일")
        }
        val weekFood2 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByFoodWeekend(month, week, Category.EATOUT.indegredients, "월")
        }
        val weekFood3 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByFoodWeekend(month, week, Category.EATOUT.indegredients, "화")
        }
        val weekFood4 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByFoodWeekend(month, week, Category.EATOUT.indegredients, "수")
        }
        val weekFood5 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByFoodWeekend(month, week, Category.EATOUT.indegredients, "목")
        }
        val weekFood6 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByFoodWeekend(month, week, Category.EATOUT.indegredients, "금")
        }
        val weekFood7 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByFoodWeekend(month, week, Category.EATOUT.indegredients, "토")
        }


        val entries3 = ArrayList<BarEntry>()
        entries3.add(BarEntry(1.1f, weekFood1))
        entries3.add(BarEntry(2.2f, weekFood2))
        entries3.add(BarEntry(3.3f, weekFood3))
        entries3.add(BarEntry(4.4f, weekFood4))
        entries3.add(BarEntry(5.5f, weekFood5))
        entries3.add(BarEntry(6.6f, weekFood6))
        entries3.add(BarEntry(7.7f, weekFood7))


        return entries3
    }

//    suspend fun initBarChartData2(month: Int, week: Int): ArrayList<BarEntry> {
//        val weekOut1 = withContext(Dispatchers.IO) {
//            appDb.JangDAO().getPriceByOutWeekend(month, week, Category.EATOUT.indegredients, "일")
//        }
//        val weekOut2 = withContext(Dispatchers.IO) {
//            appDb.JangDAO().getPriceByOutWeekend(month, week, Category.EATOUT.indegredients, "월")
//        }
//        val weekOut3 = withContext(Dispatchers.IO) {
//            appDb.JangDAO().getPriceByOutWeekend(month, week, Category.EATOUT.indegredients, "화")
//        }
//        val weekOut4 = withContext(Dispatchers.IO) {
//            appDb.JangDAO().getPriceByOutWeekend(month, week, Category.EATOUT.indegredients, "수")
//        }
//        val weekOut5 = withContext(Dispatchers.IO) {
//            appDb.JangDAO().getPriceByOutWeekend(month, week, Category.EATOUT.indegredients, "목")
//        }
//        val weekOut6 = withContext(Dispatchers.IO) {
//            appDb.JangDAO().getPriceByOutWeekend(month, week, Category.EATOUT.indegredients, "금")
//        }
//        val weekOut7 = withContext(Dispatchers.IO) {
//            appDb.JangDAO().getPriceByOutWeekend(month, week, Category.EATOUT.indegredients, "토")
//        }
//
//        val entries4 = ArrayList<BarEntry>()
//        entries4.add(BarEntry(1.2f, weekOut1))
//        entries4.add(BarEntry(2.2f, weekOut2))
//        entries4.add(BarEntry(3.2f, weekOut3))
//        entries4.add(BarEntry(4.2f, weekOut4))
//        entries4.add(BarEntry(5.2f, weekOut5))
//        entries4.add(BarEntry(5.2f, weekOut6))
//        entries4.add(BarEntry(5.2f, weekOut7))
//
//
//        return entries4
//    }

        // PieChart 를 Init 합니다. 아직 DB 구축이 되지 않은 이유로 TestData 로 테스트 합니다.
    private fun initChart(month: Int, week: Int) {

        // add a lot of colors
        val colorsItems = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.MATERIAL_COLORS) colorsItems.add(c)
        colorsItems.add(ColorTemplate.getHoloBlue())

        // Entry 초기화
        CoroutineScope(Dispatchers.Main).launch {

            val totalSum = initWeekPriceData(month,week).toInt()
            binding!!.priceView.text = "총 사용 금액 : $totalSum 원"

            val entries1 = initPieChartData1(month, week)
            val entries2 = initPieChartData2(month, week)
            val entries3 = initBarChartData1(month, week)

            binding?.apply {
                // PieChart 1
                pieChart1.setUsePercentValues(true)
                pieChart1.holeRadius = 30f
                pieChart1.setDrawEntryLabels(false)
                val pieDataSet1 = PieDataSet(entries1, "")
                pieDataSet1.apply {
                    colors = colorsItems
                    valueTextColor = Color.BLACK
                    valueTextSize = 15f
                }
                val pieData1 = PieData(pieDataSet1)
                pieChart1.apply {
                    data = pieData1
                    description.isEnabled = false
                    isRotationEnabled = false
                    centerText = ""
                    setEntryLabelColor(Color.BLACK)
                    animateY(1400, Easing.EaseInOutQuad)
                    animate()
                }

                // PieChart 2
                pieChart2.setUsePercentValues(true)
                pieChart2.holeRadius = 30f
                pieChart2.setDrawEntryLabels(false)
                val pieDataSet2 = PieDataSet(entries2, "")
                pieDataSet2.apply {
                    colors = colorsItems
                    valueTextColor = Color.BLACK
                    valueTextSize = 10f
                    formSize = 10f // 범례 항목 크기 조정 (필요에 따라 조절 가능)
                }
                val pieData2 = PieData(pieDataSet2)
                pieChart2.apply {
                    data = pieData2
                    description.isEnabled = false
                    isRotationEnabled = false
                    setEntryLabelColor(Color.BLACK)
                    legend.apply {
                        isWordWrapEnabled = true // 범례 줄 바꿈 활성화
                        textSize = 10f // 범례 항목 텍스트 크기 조정 (필요에 따라 조절 가능)
                    }
                    animateY(1400, Easing.EaseInOutQuad)
                    animate()
                }

                var set1 = BarDataSet(entries3, "DataSet1")

                val data = BarData(set1) // 두 개의 DataSet을 하나의 BarData로 설정

                data.barWidth = 0.8f // 각 막대의 너비 설정

                barChart.run {

                    description.isEnabled = false
                    setMaxVisibleValueCount(7)
                    setPinchZoom(false)
                    setDrawBarShadow(false)
                    setDrawGridBackground(false)
                    axisLeft.run {
                        axisMaximum = 400000f
                        axisMinimum = 0f
                        granularity = 50000f
                        setDrawLabels(true)
                        setDrawGridLines(true)
                        setDrawAxisLine(false)
                        axisLineColor =
                            ContextCompat.getColor(context, com.example.foodjoa.R.color.black)
                        gridColor =
                            ContextCompat.getColor(context, com.example.foodjoa.R.color.black)
                        textColor =
                            ContextCompat.getColor(context, com.example.foodjoa.R.color.black)
                        textSize = 14f
                    }


                    xAxis.run {
                        position = XAxis.XAxisPosition.BOTTOM
                        granularity = 1.1f
                        setDrawAxisLine(true)
                        setDrawGridLines(false)
                        textColor = ContextCompat.getColor(
                            context,
                            com.google.android.material.R.color.design_default_color_primary_dark
                        )
                        textSize = 10f
                        labelCount = 7
                        valueFormatter = MyXAxisFormatter()
                    }


                    axisRight.isEnabled = false
                    setTouchEnabled(false)
                    animateY(1000)
                    legend.isEnabled = false
                    this.data = data // BarData를 설정
                    setFitBars(true)
                    invalidate()
                }

            }
        }

    }
}