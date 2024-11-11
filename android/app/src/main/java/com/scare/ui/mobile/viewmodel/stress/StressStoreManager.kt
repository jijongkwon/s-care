package com.scare.ui.mobile.viewmodel.stress

import android.content.Context
import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.scare.data.heartrate.api.request.CreateDailyStressReq
import com.scare.data.heartrate.dao.HeartRateDao
import com.scare.data.heartrate.database.entity.HeartRate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StressStoreManager(
    private val context: Context,
    private val heartRateDao: HeartRateDao
) {

    fun calculateDailyStressSince(lastSaveDate: LocalDateTime): List<CreateDailyStressReq> {
        // lastSaveDate를 기준으로 시작 날짜 설정
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val startDateTime = lastSaveDate.format(formatter)

        // 전날 자정을 끝 날짜로 설정
        val endDateTime = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59).format(formatter)

        // 지정한 날짜 범위의 심박수 데이터를 가져옴
        val heartRates = heartRateDao.getHeartRateSince(startDateTime, endDateTime)

        Log.d("heartRateData", heartRates.toString())

        // 날짜별로 필터링하여 요청 생성
        val dailyStressRequests = mutableListOf<CreateDailyStressReq>() //최종으로 보낼 데이터

        //해당 날짜만
        val uniqueDates = heartRates.map { it.createdAt.toLocalDate() }.distinct()

        uniqueDates.forEach { date ->
            val heartRatesForDate = heartRates.filter { it.createdAt.toLocalDate() == date }

            Log.d("StressStoreManager", "${heartRatesForDate}")

            val stressLevel = calculateStress(heartRatesForDate)

            Log.d("StressStoreManager", "${stressLevel}")

            //-1이 아닌 경우에만..
            if(stressLevel != -1) {
                dailyStressRequests.add(
                    CreateDailyStressReq(
                        recordedAt = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                        stress = stressLevel
                    )
                )
            }
        }

        Log.d("StressStoreManager", "dailyStressRequests:${dailyStressRequests}")

        return dailyStressRequests
    }
    
    //스트레스 계산
    fun calculateStress (heartRatesForDate: List<HeartRate>): Int {
        //300개 이상의 배열이 있는 경우에만. . .
        if (heartRatesForDate.size >= 300) {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(context))
            }

            val py = Python.getInstance()
            val pyModule = py.getModule("calc_stress")
            val result: PyObject = pyModule.callAttr("get_single_stress", heartRatesForDate)
            val stress = result.toInt()

            return stress;
        }

        return -1
    }
}