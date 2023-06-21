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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CostFragmentByMonth : Fragment() {

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
        initChart(month) // 처음엔 현재 월의 데이터를 가져옴
        initSpinner()
    }


    private fun initSpinner() {
        setHasOptionsMenu(true)

        // 스피너 어댑터 설정
        val months = arrayOf("1월", "2월", "3월", "4월",
            "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
        val monthInt = arrayOf(1,2,3,4,5,6,7,8,9,10,11,12)
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, months)
        binding!!.monthSpinner.adapter = spinnerAdapter

        // 현재 월을 기본값으로 설정
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val defaultPosition = monthInt.indexOf(currentMonth)
        binding!!.monthSpinner.setSelection(defaultPosition)


        // 스피너 선택 이벤트 리스너 설정
        binding!!.monthSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMonth = monthInt[position]
                initChart(selectedMonth) // 선택된 월에 대한 차트 업데이트
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않을 때가 뭐지 ???
            }
        }
    }







// ============================================================================================




    inner class MyXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("1주차","2주차","3주차","4주차","5주차")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }

    suspend fun initMonthPriceData(month: Int): Float {
        val totalSum = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByMonth(month)
        }
        return totalSum
    }

    suspend fun initPieChartData1(month: Int): ArrayList<PieEntry> {

        val outExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByCategory(month, Category.EATOUT.indegredients)
        }

        val foodExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByFood(month, Category.EATOUT.indegredients)
        }


        val entries1 = ArrayList<PieEntry>()
        entries1.add(PieEntry(outExpense, "외식류"))
        entries1.add(PieEntry(foodExpense, "식품류"))
        return entries1
    }

    suspend fun initPieChartData2(month: Int): ArrayList<PieEntry> {
        val meatExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByCategory(month, Category.MEAT.indegredients)
        }
        val fishExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByCategory(month, Category.SEAFOOD.indegredients)
        }
        val fruitExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByCategory(month, Category.FRUITVEG.indegredients)
        }
        val snackExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByCategory(month, Category.SNACK.indegredients)
        }

        val frozenExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByCategory(month, Category.FROZENFOOD.indegredients)
        }
        val drinkExpense = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByCategory(month, Category.DRINK.indegredients)
        }


        val entries2 = ArrayList<PieEntry>()
        entries2.add(PieEntry(meatExpense, "고기류"))
        entries2.add(PieEntry(fishExpense, "해산물류"))
        entries2.add(PieEntry(fruitExpense, "과일야채류"))
        entries2.add(PieEntry(snackExpense, "과자/빙과류"))
        entries2.add(PieEntry(frozenExpense, "냉동식품류"))
        entries2.add(PieEntry(drinkExpense, "음료류"))

        return entries2
    }

    suspend fun initBarChartData(month: Int): ArrayList<BarEntry> {

        val week1 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeek(month, 1)
        }
        val week2 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeek(month, 2)
        }
        val week3 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeek(month, 3)
        }
        val week4 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeek(month, 4)
        }
        val week5 = withContext(Dispatchers.IO) {
            appDb.JangDAO().getPriceByWeek(month, 5)
        }

        val entries3 = ArrayList<BarEntry>()
        entries3.add(BarEntry(1.0f, week1))
        entries3.add(BarEntry(2.0f, week2))
        entries3.add(BarEntry(3.0f, week3))
        entries3.add(BarEntry(4.0f, week4))
        entries3.add(BarEntry(5.0f, week5))

        return entries3
    }

    // PieChart 를 Init 합니다. 아직 DB 구축이 되지 않은 이유로 TestData 로 테스트 합니다.
    private fun initChart(month: Int) {

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
            val entries1 = initPieChartData1(month)
            val entries2 = initPieChartData2(month)
            val entries3 = initBarChartData(month)

            val totalSum = initMonthPriceData(month).toInt()
            binding!!.priceView.text = "총 사용 금액 : $totalSum 원"

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



                // BarChart
                barChart.run {
                    description.isEnabled = false
                    setMaxVisibleValueCount(5)
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
                        position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                        granularity = 1f // 1 단위만큼 간격 두기
                        setDrawAxisLine(true) // 축 그림
                        setDrawGridLines(false) // 격자
                        textColor = ContextCompat.getColor(
                            context,
                            com.google.android.material.R.color.design_default_color_primary_dark
                        ) //라벨 색상
                        textSize = 10f // 텍스트 크기
                        valueFormatter = MyXAxisFormatter() // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
                    }
                    axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
                    setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
                    animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
                    legend.isEnabled = false //차트 범례 설정
                }
                var set = BarDataSet(entries3, "DataSet") // 데이터셋 초기화
                val dataSet : ArrayList<IBarDataSet> = ArrayList()
                dataSet.add(set)
                val data = BarData(dataSet)
                data.barWidth = 0.5f //막대 너비 설정
                barChart.run {
                    this.data = data //차트의 데이터를 data로 설정해줌.
                    setFitBars(true)
                    invalidate()
                }

            }
        }

    }
}