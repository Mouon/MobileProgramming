package com.example.foodjoa.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.foodjoa.R
import com.example.foodjoa.database.AppDatabase
import com.example.foodjoa.database.JANG
import com.example.foodjoa.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeFragment : Fragment() {
    var binding : FragmentHomeBinding?=null
    val handler = Handler(Looper.getMainLooper())
    lateinit var appDb: AppDatabase
    lateinit var fadeInAnim : Animation
    lateinit var fadeOutAnim : Animation
    lateinit var fadeInAnim2 : Animation
    lateinit var fadeOutAnim2: Animation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appDb = AppDatabase.getAppDatabase(context)
    }

    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val calendar: Calendar = Calendar.getInstance()
    val dates = sdf.format(Date()).split("-".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()
    val year = dates[0].toInt()
    val month = dates[1].toInt()
    val day = dates[2].toInt()
    private fun getWeekOfYear(date: String): Int {
        calendar.set(year, month - 1, day)
        return calendar.get(Calendar.WEEK_OF_MONTH)
    }
    private fun getLastWeekOfMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // 해당 달의 첫째 날로 설정
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) // 해당 달의 마지막 날
        calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth) // 해당 날짜로 설정
        return calendar.get(Calendar.WEEK_OF_MONTH) // 주차 반환
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apply {
            moneybtn.setOnClickListener {
                val thisWeek = getWeekOfYear(sdf.format(Date()))
                CoroutineScope(Dispatchers.Main).launch {
                    val outExpense = withContext(Dispatchers.IO) {
                        appDb.JangDAO().getWeekPriceByWeek(thisWeek)
                    }
                    moneybub1.setText("이번 주 얼마를 사용했는지 보고싶어요")
                    moneybub2.setText("${thisWeek}주차에 ${outExpense.toInt()}원만큼 지출하셨습니다.")
                }

                fadeInAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.money_fade_in)
                fadeOutAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.money_fade_out)
                fadeInAnim2 = AnimationUtils.loadAnimation(requireActivity(), R.anim.money_fade_in2)
                fadeOutAnim2 =
                    AnimationUtils.loadAnimation(requireActivity(), R.anim.money_fade_out2)
                if (moneyLayout1.visibility == View.VISIBLE &&  moneyLayout2.visibility == View.VISIBLE) {
                    moneyLayout1.startAnimation(fadeOutAnim2)
                    moneyLayout2.startAnimation(fadeOutAnim)
                    handler.postDelayed({
                        moneyLayout1.visibility = View.GONE
                    }, 500)
                    handler.postDelayed({
                        moneyLayout2.visibility = View.GONE
                    },1000)

                } else {
                    moneyLayout1.startAnimation(fadeInAnim)
                    moneyLayout2.startAnimation(fadeInAnim2)
                    moneyLayout1.visibility = View.VISIBLE
                    handler.postDelayed({
                        moneyLayout2.visibility = View.VISIBLE
                    },500)
                }
            }
            reservebtn.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val outExpense = withContext(Dispatchers.IO) {
                        appDb.JangDAO().getDataByToday(year,month,day)
                    }
                    reservebub1.setText("내가 한 예약을 보여주세요!")
                    if(outExpense.isEmpty()){
                        reservebub2.setText("오늘 이후로 예약된 항목이 없습니다.\n 예약 서비스를 이용해보세요.")
                    }
                    else {
                        reservebub2.setText("")
                        for (jang: JANG in outExpense) {

                            val year=jang.year
                            val month = jang.month
                            val day = jang.day
                            val productname = jang.productname
                            val count = jang.count
                            val countunit = jang.countunit
                            lateinit var countunit_string: String
                            when (countunit) {
                                0 -> {
                                    countunit_string = "개"
                                }
                                1 -> {
                                    countunit_string = "마리"
                                }
                                2 -> {
                                    countunit_string = "그램"
                                }
                            }
                            if(reservebub2.text.isNotEmpty()){
                                reservebub2.append("\n")
                            }
                            reservebub2.append("${month}월 ${day}일에 ${productname} ${count}${countunit_string} 예약하셨습니다.")
                        }

                    }

                }
                fadeInAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.reserve_fade_in)
                fadeOutAnim =
                    AnimationUtils.loadAnimation(requireActivity(), R.anim.resereve_fade_out)
                fadeInAnim2 =
                    AnimationUtils.loadAnimation(requireActivity(), R.anim.reserve_fade_in2)
                fadeOutAnim2 =
                    AnimationUtils.loadAnimation(requireActivity(), R.anim.reserve_fade_out2)
                if (reserveLayout1.visibility == View.VISIBLE &&  reserveLayout2.visibility == View.VISIBLE) {
                    reserveLayout1.startAnimation(fadeOutAnim2)
                    reserveLayout2.startAnimation(fadeOutAnim)
                    handler.postDelayed({
                        reserveLayout1.visibility = View.GONE
                    }, 500)
                    handler.postDelayed({
                        reserveLayout2.visibility = View.GONE
                    },1000)

                } else {
                    reserveLayout1.startAnimation(fadeInAnim)
                    reserveLayout2.startAnimation(fadeInAnim2)
                    reserveLayout1.visibility = View.VISIBLE
                    handler.postDelayed({
                        reserveLayout2.visibility = View.VISIBLE
                    },500)
                }
            }
            recommandbtn.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val outProduct1 = withContext(Dispatchers.IO) {
                        appDb.JangDAO().findMostOrderedProductOfYear(year)
                    }
                    val new_month=month-1
                    val outProduct2 = withContext(Dispatchers.IO) {
                        appDb.JangDAO().findMostOrderedProductOfLastMonth(year,new_month)
                    }
                    val outProduct3 = withContext(Dispatchers.IO) {
                        if(getWeekOfYear(sdf.format(Date()))==1){
                            val lastWeekofMonth=getLastWeekOfMonth(year,month-1)
                            appDb.JangDAO().findMostOrderedProductOfLastWeek(year,month,lastWeekofMonth)
                        }
                        else{
                            appDb.JangDAO().findMostOrderedProductOfLastWeek(year,month,getWeekOfYear(sdf.format(Date()))-1)
                        }

                    }
                    Log.d(TAG,"$outProduct1")
                    recommandbub1.setText("추천할만한 품목들을 알려주세요")
                    recommandbub2.setText("다음과 같은 항목들을 추천드리겠습니다.\n---------------------------------\n")
                    if(outProduct1!=null||outProduct2!=null||outProduct3!=null){
                        if(outProduct1==null) {
                            ;
                        }
                        else if(outProduct1=="배달"||outProduct1=="외식"||outProduct1=="외식기타"){
                            recommandbub2.append("올해 ${outProduct1}에 가장 많이 사용하셨습니다\n 다른 것을 구매해보시는 것은 어떨까요?")
                        }
                        
                        else {
                            recommandbub2.append("올해 가장 많이 예약하신 상품\n${outProduct1}를 구매해보시는 것은 어떨까요?")
                        }

                        if (outProduct2==null) {
                            recommandbub2.append("")
                        }

                        else {
                            if(outProduct2=="배달"||outProduct2=="외식"||outProduct2=="외식기타"){
                                recommandbub2.append("\n---------------------------------\n")
                                recommandbub2.append("저번 달에 ${outProduct2}에 가장 많이 사용하셨습니다\n 이번 달은 다른 것을 구매해보시는 것은 어떨까요?")
                            }
                            else {
                                recommandbub2.append("\n---------------------------------\n")
                                recommandbub2.append("저번 달에 가장 많이 구매하신\n ${outProduct2}를 구매해보시는 것은 어떨까요?")
                            }
                        }

                        if (outProduct3==null) {
                            recommandbub2.append("")
                        }

                        else {
                            if(outProduct3=="배달"||outProduct3=="외식"||outProduct3=="외식기타"){
                                recommandbub2.append("\n---------------------------------\n")
                                recommandbub2.append("저번 주에 ${outProduct3}에 가장 많이 사용하셨습니다\n 이번 주는 다른 것을 구매해보시는 것은 어떨까요?")
                            }
                            else {
                                recommandbub2.append("\n---------------------------------\n")
                                recommandbub2.append("저번 주에 가장 많이 구매하신\n ${outProduct3}를 구매해보시는 것은 어떨까요?")
                            }
                        }

                    }
                    else {
                        recommandbub2.setText("유감이지만 예약하신 정보가 부족하여\n추천드릴 제품이 없습니다.")
                    }


                }
                fadeInAnim =
                    AnimationUtils.loadAnimation(requireActivity(), R.anim.recommand_fade_in)
                fadeOutAnim =
                    AnimationUtils.loadAnimation(requireActivity(), R.anim.recommand_fade_out)
                fadeInAnim2 =
                    AnimationUtils.loadAnimation(requireActivity(), R.anim.recommand_fade_in2)
                fadeOutAnim2 =
                    AnimationUtils.loadAnimation(requireActivity(), R.anim.recommand_fade_out2)
                if (recommandLayout1.visibility == View.VISIBLE &&  recommandLayout2.visibility == View.VISIBLE) {
                    recommandLayout1.startAnimation(fadeOutAnim2)
                    recommandLayout2.startAnimation(fadeOutAnim)
                    handler.postDelayed({
                        recommandLayout1.visibility = View.GONE
                    }, 500)
                    handler.postDelayed({
                        recommandLayout2.visibility = View.GONE
                    },1000)

                } else {
                    recommandLayout1.startAnimation(fadeInAnim)
                    recommandLayout2.startAnimation(fadeInAnim2)
                    recommandLayout1.visibility = View.VISIBLE
                    handler.postDelayed({
                        recommandLayout2.visibility = View.VISIBLE
                    },500)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}