package com.example.foodjoa.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JangDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItem(jang: JANG)

    @Query("DELETE FROM bag WHERE itemid = :ItemId")
    fun delete(ItemId : Int)

    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH")
    fun getPriceByMonth(MONTH: Int): Float

    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH AND week = :WEEK")
    fun getPriceByMonthAndWeek(MONTH: Int, WEEK: Int): Float
    //날짜에 관련해서 오늘 날짜 이후 예약 가져오기
    @Query("SELECT * FROM bag WHERE ((year > :YEAR) OR (year = :YEAR AND month > :MONTH) OR (year = :YEAR AND month = :MONTH AND day > :DAY)) AND category != '외식' LIMIT 3")
    fun getDataByToday(YEAR: Int, MONTH: Int, DAY: Int): List<JANG>

    @Query("SELECT * FROM bag WHERE year = :YEAR AND month = :MONTH AND day = :DAY")
    fun getDataToPushToday(YEAR: Int, MONTH: Int, DAY: Int): List<JANG>

    //같은 주차에 대한 가격 가져오기
    @Query("SELECT SUM(price) FROM bag WHERE week= :WEEK")
    fun getWeekPriceByWeek(WEEK: Int):Float

    @Query("SELECT productname FROM bag WHERE year = :YEAR GROUP BY productname ORDER BY COUNT(*) DESC LIMIT 1")
    fun findMostOrderedProductOfYear(YEAR: Int): String

    @Query("SELECT productname FROM bag WHERE month=:MONTH AND year=:YEAR GROUP BY productname ORDER BY COUNT(*) DESC LIMIT 1")
    fun findMostOrderedProductOfLastMonth(YEAR: Int, MONTH: Int): String

    @Query("SELECT productname FROM bag WHERE week=:WEEK AND year=:YEAR AND month=:MONTH GROUP BY productname ORDER BY COUNT(*) DESC LIMIT 1")
    fun findMostOrderedProductOfLastWeek(YEAR: Int, MONTH: Int,WEEK: Int): String

    @Query("SELECT productname FROM bag WHERE week=:WEEK AND year=:YEAR AND month=:MONTH")
    fun findMostOrderedProductOfLastWeektoNext(YEAR: Int, MONTH: Int,WEEK: Int): List<String>
    //여기까지
    // 월별 카테고리별 금액 총 합 찾기
    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH AND category = :CATEGORY")
    fun getPriceByCategory(MONTH: Int, CATEGORY: String): Float

    // 월별 식료품비 금액 총 합 찾기
    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH AND category <> :CATEGORY")
    fun getPriceByFood(MONTH:Int, CATEGORY:String): Float

    // 월별 식료품비 금액 총 합 찾기
    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH AND week = :WEEK")
    fun getPriceByWeek(MONTH:Int, WEEK:Int): Float

    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH AND week = :WEEK AND category = :CATEGORY")
    fun getPriceByWeekAndCategory(MONTH: Int, WEEK: Int, CATEGORY: String): Float

    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH AND week = :WEEK AND category <> :CATEGORY")
    fun getPriceByWeekByFood(MONTH: Int, WEEK: Int, CATEGORY: String): Float

    @Query("SELECT * FROM bag WHERE month = :MONTH AND day = :DAY")
    fun getItemsToday(MONTH: Int, DAY: Int): List<JANG>

    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH AND week = :WEEK AND day = :DAY")
    fun getPriceTodayByWeek(MONTH: Int, WEEK: Int, DAY: Int): Float

    @Query("SELECT * FROM bag WHERE month = :MONTH AND day = :DAY and category = :CATEGORY")
    fun getItemByDayAndCategory(MONTH: Int, DAY: Int, CATEGORY: String): List<JANG>

    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH AND week = :WEEK AND category <> :CATEGORY AND weekend = :WEEKEND")
    fun getPriceByFoodWeekend(MONTH: Int, WEEK: Int, CATEGORY: String, WEEKEND: String): Float

    @Query("SELECT SUM(price) FROM bag WHERE month = :MONTH AND week = :WEEK AND category == :CATEGORY AND weekend = :WEEKEND")
    fun getPriceByOutWeekend(MONTH: Int, WEEK: Int, CATEGORY: String, WEEKEND: String): Float


}