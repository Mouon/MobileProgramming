package com.example.foodjoa

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.foodjoa.activities.MainActivity
import com.example.foodjoa.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MyReceiver : BroadcastReceiver() {
    private lateinit var userDatabase: AppDatabase
    companion object {
        // 아이디 선언
        const val NOTIFICATION_ID = 0
        const val CHANNEL_ID = "notification_channel"

        // 알림 시간 설정
        const val ALARM_TIMER = 5

    }
    var lastweekdata = ""
    var ismsg= 0
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

    lateinit var notificationManager: NotificationManager
    override fun onReceive(context: Context, intent: Intent) {
        userDatabase = AppDatabase.getAppDatabase(context)
        var msg = intent.getStringExtra("msg")!!
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        createNotificationChannel()
        deliverNotification(context, msg)

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

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextAlarmTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000 // 현재 시간에서 24시간 뒤
        intent.putExtra("msg", msg)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextAlarmTime,
            pendingIntent
        )
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, // 채널의 아이디
                "채널 이름입니다.", // 채널의 이름
                NotificationManager.IMPORTANCE_HIGH
                /*
                1. IMPORTANCE_HIGH = 알림음이 울리고 헤드업 알림으로 표시
                2. IMPORTANCE_DEFAULT = 알림음 울림
                3. IMPORTANCE_LOW = 알림음 없음
                4. IMPORTANCE_MIN = 알림음 없고 상태줄 표시 X
                 */
            )
            notificationChannel.enableLights(true) // 불빛
            notificationChannel.lightColor = Color.RED // 색상
            notificationChannel.enableVibration(true) // 진동 여부
            notificationChannel.description = "채널의 상세정보입니다." // 채널 정보
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }

    }

    private fun deliverNotification(context: Context, msg: String){
        val contentIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID, // requestCode
            contentIntent, // 알림 클릭 시 이동할 인텐트
            PendingIntent.FLAG_UPDATE_CURRENT
            /*
            1. FLAG_UPDATE_CURRENT : 현재 PendingIntent를 유지하고, 대신 인텐트의 extra data는 새로 전달된 Intent로 교체
            2. FLAG_CANCEL_CURRENT : 현재 인텐트가 이미 등록되어있다면 삭제, 다시 등록
            3. FLAG_NO_CREATE : 이미 등록된 인텐트가 있다면, null
            4. FLAG_ONE_SHOT : 한번 사용되면, 그 다음에 다시 사용하지 않음
             */
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_home_24) // 아이콘
            .setContentTitle("FOODJOA") // 제목
            .setContentText("아래로 스크롤 하세요") // 내용
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("오늘 사야할 물품 : $msg"))
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }



}

