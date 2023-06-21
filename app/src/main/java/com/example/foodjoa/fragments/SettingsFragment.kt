package com.example.foodjoa.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.foodjoa.Category
import com.example.foodjoa.MyReceiver
import com.example.foodjoa.activities.ChangePasswordActivity
import com.example.foodjoa.activities.LoginActivity
import com.example.foodjoa.database.AppDatabase
import com.example.foodjoa.database.UserDAO
import com.example.foodjoa.databinding.FragmentSettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userId: String
    private lateinit var userNickname: String
    private lateinit var context: Context
    private lateinit var userDatabase: AppDatabase
    private lateinit var userDAO: UserDAO
    private lateinit var nickname: String
    var lastweekdata = ""
    var msg = ""
    var ismsg = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    companion object {
        private const val ARG_USER_ID = "user_id"
        private const val ARG_USER_NICKNAME = "user_nickname"

        fun newInstance(userId: String?, userNickname: String?): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putString(ARG_USER_ID, userId)
            args.putString(ARG_USER_NICKNAME, userNickname)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(ARG_USER_ID, "")
            userNickname = it.getString(ARG_USER_NICKNAME, "")
        } ?: run {
            userId = ""
            userNickname = ""
        }

        userDatabase = AppDatabase.getAppDatabase(context)
        userDAO = userDatabase.UserDAO()
    }

    private fun updateUI(userId: String, userNickname: String) {
        binding.idTextView.text = userId
        binding.nicknameTextView.text = userNickname
    }

    private fun logout() {
        // 로그아웃 로직을 여기에 구현합니다.
        // 예를 들어, 현재 사용자를 로그아웃 처리하고 로그인 화면으로 이동하는 코드를 작성할 수 있습니다.
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // 현재 액티비티를 종료하여 이전 세션을 완전히 닫습니다.
    }

    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA) // 오늘 날짜
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
        calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth) //해당 날짜로 설정
        return calendar.get(Calendar.WEEK_OF_MONTH) // 주차 반환
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val userId = arguments?.getString(ARG_USER_ID, "") ?: ""
        val userNickname = arguments?.getString(ARG_USER_NICKNAME, "") ?: ""
        updateUI(userId, userNickname)
        CoroutineScope(Dispatchers.IO).launch {
            val result = withContext(Dispatchers.IO) {
                if (getWeekOfYear(sdf.format(Date())) == 1) {
                    val lastWeekofMonth = getLastWeekOfMonth(year, month - 1)
                    userDatabase.JangDAO().findMostOrderedProductOfLastWeektoNext(
                        year,
                        month,
                        lastWeekofMonth
                    )
                } else {
                    userDatabase.JangDAO().findMostOrderedProductOfLastWeektoNext(
                        year, month, getWeekOfYear(sdf.format(Date())) - 1
                    )

                }

            }
            val result2 = withContext(Dispatchers.IO) {
                userDatabase.JangDAO().getDataToPushToday(year,month, day)
            }
            if (result2.isEmpty()) {
                ismsg = 0
            }
            else {
                for ((index2, item2) in result2.withIndex()){
                    val productname = item2.productname
                    val count = item2.count
                    val countunit = item2.countunit
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
                    if (productname != Category.EATOUT.indegredients)
                        lastweekdata += "$productname $count$countunit_string"

                    if (index2 < result2.size -1) {
                        lastweekdata += ", "
                    }
                }
                msg = "오늘 사야할 품목은 $lastweekdata 입니다! \n"
                lastweekdata = ""
                ismsg =1
                if (result.isNotEmpty()) {
                    for ((index, item) in result.withIndex()) {
                        if (item != Category.EATOUT.indegredients)
                            lastweekdata += item
                        if (index < result.size - 1) {
                            lastweekdata += ", "
                        }
                    }

                    msg += "저번 주에 산 ${lastweekdata}을/를 구매하시는걸 추천드려요!"
                }
            }
        }

        var checked = false
        binding.apply {
            timePicker.setIs24HourView(true)
            CoroutineScope(Dispatchers.IO).launch {
                timePicker.hour = userDatabase.UserDAO().getHour(userId)
                timePicker.minute = userDatabase.UserDAO().getMinute(userId)
                checked = userDatabase.UserDAO().getIsChecked(userId)
                withContext(Dispatchers.Main) {
                    alarmSwitch.setChecked(checked)
                }

            }

        }

        binding.timepickerokbtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                userDatabase.UserDAO().updateHourById(userId, binding.timePicker.hour)
                userDatabase.UserDAO().updateMinuteById(userId, binding.timePicker.minute)
                withContext(Dispatchers.Main) {
                    makeNotify(checked)
                }
            }

        }
        // onCreateView() 메서드에서 Switch에 대한 체크 상태 변경 리스너 설정
        binding.alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                userDatabase.UserDAO().updateCheckedById(userId, isChecked)
            }
        }



        binding.changePasswordButton.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        binding.logoutButton.setOnClickListener {
            // 로그아웃 처리를 수행하는 메서드 호출
            logout()
        }
        // 코루틴을 사용하여 백그라운드에서 데이터베이스 액세스 실행
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                nickname = userDAO.getNicknameById(userId)
            }
            binding.nicknameTextView.text = nickname
        }



        return binding.root
    }

    fun makeNotify(checked:Boolean) {
        val timepicker_hour = binding.timePicker.hour
        val timepicker_minute = binding.timePicker.minute
        val alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(), MyReceiver::class.java)
        intent.putExtra("msg", msg)
        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (checked) {
            if (ismsg == 1) {

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, timepicker_hour)
                    set(Calendar.MINUTE, timepicker_minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val repeatInterval: Long = MyReceiver.ALARM_TIMER * 1000L
                val triggerTime = (SystemClock.elapsedRealtime()
                        + repeatInterval)

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )


            } else {

            }

        } else {
            alarmManager.cancel(pendingIntent)
        }
    }



}