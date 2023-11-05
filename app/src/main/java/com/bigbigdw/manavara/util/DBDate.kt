package com.bigbigdw.manavara.util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DBDate {

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    fun dateYYYY(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("YYYY")
        return format.format(currentTime).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun dateMM(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("MM")
        return format.format(currentTime).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun datedd(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("dd")
        return format.format(currentTime).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun dateYesterday(): String {
        // 현재 날짜 가져오기
        val currentDate = LocalDate.now()

        // 어제 날짜 계산
        val yesterday = currentDate.minusDays(1)

        return yesterday.toString().replace("-","")
    }

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    fun dateMMDD(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("YYYYMMdd")
        return format.format(currentTime).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun dateMMDDHHMM(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("YYYYMMddHHmm")
        return format.format(currentTime).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun dateMMDDHHMMss(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("YYYYMMddHHmmss")
        return format.format(currentTime).toString()
    }

    fun year() : String{
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year

        return currentYear.toString()
    }

    fun month(): String {
        val currentDate = LocalDate.now()
        val currentMonth = currentDate.monthValue
        return currentMonth.toString()
    }


    fun getWeekDates(year: Int, month: Int, weekNumber: Int): List<LocalDate> {
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val weekFields = WeekFields.of(Locale.getDefault())

        // 해당 월의 첫 번째 주의 시작일을 찾습니다.
        val firstDayOfFirstWeek = firstDayOfMonth
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .with(weekFields.weekOfYear(), 1)

        // 요청된 주차의 시작일과 마지막일을 계산합니다.
        val startDate = firstDayOfFirstWeek.plusWeeks(weekNumber.toLong() - 1)
        val endDate = startDate.plusDays(6)

        val weekDates = mutableListOf<LocalDate>()
        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            weekDates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }

        return weekDates
    }

    fun getCurrentWeekNumber(): Int {
        val currentDate = LocalDate.now()
        val weekFields = WeekFields.of(Locale.getDefault())

        // 현재 날짜의 주 번호를 가져옵니다.
        return currentDate.get(weekFields.weekOfMonth())
    }

    fun getDayOfWeekAsNumber(): Int {
        val currentDate = LocalDate.now()
        val dayOfWeek = currentDate.dayOfWeek

        return if(dayOfWeek.value > 6){
            0
        } else {
            dayOfWeek.value
        }
    }

    fun getYesterdayDayOfWeek(): Int {
        val currentDate = LocalDate.now()
        val yesterday = currentDate.minusDays(1)
        val dayOfWeek = yesterday.dayOfWeek
        return dayOfWeek.value // 요일을 숫자로 얻습니다. (1: 월요일, 2: 화요일, ..., 7: 일요일)
    }

    fun getNumberOfWeeksInMonth(year: Int, month: Int): Int {
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val lastDayOfMonth = LocalDate.of(year, month, firstDayOfMonth.lengthOfMonth())

        val weekFields = WeekFields.of(Locale.getDefault())

        val firstWeekNumber = firstDayOfMonth.get(weekFields.weekOfWeekBasedYear())
        val lastWeekNumber = lastDayOfMonth.get(weekFields.weekOfWeekBasedYear())

        return lastWeekNumber - firstWeekNumber + 1
    }

    @SuppressLint("SimpleDateFormat")
    fun dateYesterdayMMDD(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1) // 오늘 날짜에서 1일을 빼서 어제 날짜를 얻습니다.
        val yesterday = calendar.time

        val format = SimpleDateFormat("yyyyMMdd")
        return format.format(yesterday)
    }

}