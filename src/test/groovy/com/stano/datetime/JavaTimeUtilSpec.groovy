package com.stano.datetime

import spock.lang.Specification

import java.sql.Time
import java.sql.Timestamp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId

public class JavaTimeUtilSpec extends Specification {

   void testJavaDateToLocalDate() {

      def localDate = JavaTimeUtil.javaDateToLocalDate(javaDate)

      expect:
      localDate.year == year
      localDate.month == month
      localDate.dayOfMonth == dayOfMonth

      where:
      javaDate                          | year | month       | dayOfMonth
      makeDate(2013, 3, 17, 0, 0, 0)    | 2013 | Month.MARCH | 17
      makeDate(2013, 3, 17, 8, 0, 0)    | 2013 | Month.MARCH | 17
      makeDate(2013, 3, 17, 16, 47, 0)  | 2013 | Month.MARCH | 17
      makeDate(2013, 3, 17, 18, 30, 45) | 2013 | Month.MARCH | 17
      makeDate(2013, 3, 17, 23, 59, 59) | 2013 | Month.MARCH | 17
   }

   void testJavaDateToLocalDateTime() {

      def localDateTime = JavaTimeUtil.javaDateToLocalDateTime(javaDate)

      expect:
      localDateTime.year == year
      localDateTime.month == month
      localDateTime.dayOfMonth == dayOfMonth
      localDateTime.hour == hour
      localDateTime.minute == minute
      localDateTime.second == second

      where:
      javaDate                          | year | month       | dayOfMonth | hour | minute | second
      makeDate(2013, 3, 17, 0, 0, 0)    | 2013 | Month.MARCH | 17         | 0    | 0      | 0
      makeDate(2013, 3, 17, 8, 0, 0)    | 2013 | Month.MARCH | 17         | 8    | 0      | 0
      makeDate(2013, 3, 17, 16, 47, 0)  | 2013 | Month.MARCH | 17         | 16   | 47     | 0
      makeDate(2013, 3, 17, 18, 30, 45) | 2013 | Month.MARCH | 17         | 18   | 30     | 45
   }

   void testJavaDateToLocalTime() {

      def localTime = JavaTimeUtil.javaDateToLocalTime(javaDate)

      expect:
      localTime.hour == hour
      localTime.minute == minute
      localTime.second == second

      where:
      javaDate                          | hour | minute | second
      makeDate(2013, 3, 17, 0, 0, 0)    | 0    | 0      | 0
      makeDate(2013, 3, 17, 8, 0, 0)    | 8    | 0      | 0
      makeDate(2013, 3, 17, 16, 47, 0)  | 16   | 47     | 0
      makeDate(2013, 3, 17, 18, 30, 45) | 18   | 30     | 45
   }

   void testSqlDateToLocalDate() {

      def localDate = JavaTimeUtil.sqlDateToLocalDate(sqlDate)

      expect:
      localDate.year == year
      localDate.month == month
      localDate.dayOfMonth == dayOfMonth

      where:
      sqlDate               | year | month           | dayOfMonth
      makeSqlDate(2013, 1, 17)  | 2013 | Month.JANUARY   | 17
      makeSqlDate(2013, 2, 17)  | 2013 | Month.FEBRUARY  | 17
      makeSqlDate(2013, 3, 17)  | 2013 | Month.MARCH     | 17
      makeSqlDate(2013, 4, 17)  | 2013 | Month.APRIL     | 17
      makeSqlDate(2013, 5, 17)  | 2013 | Month.MAY       | 17
      makeSqlDate(2013, 6, 17)  | 2013 | Month.JUNE      | 17
      makeSqlDate(2013, 7, 17)  | 2013 | Month.JULY      | 17
      makeSqlDate(2013, 8, 17)  | 2013 | Month.AUGUST    | 17
      makeSqlDate(2013, 9, 17)  | 2013 | Month.SEPTEMBER | 17
      makeSqlDate(2013, 10, 17) | 2013 | Month.OCTOBER   | 17
      makeSqlDate(2013, 11, 17) | 2013 | Month.NOVEMBER  | 17
      makeSqlDate(2013, 12, 17) | 2013 | Month.DECEMBER  | 17
   }

   void testSqlTimeToLocalTime() {

      def localTime = JavaTimeUtil.sqlTimeToLocalTime(sqlTime)

      expect:
      localTime.hour == hour
      localTime.minute == minute
      localTime.second == second

      where:
      sqlTime             | hour | minute | second
      makeSqlTime(0, 0, 0)    | 0    | 0      | 0
      makeSqlTime(8, 0, 0)    | 8    | 0      | 0
      makeSqlTime(16, 47, 0)  | 16   | 47     | 0
      makeSqlTime(18, 30, 45) | 18   | 30     | 45
   }

   void testSqlTimestampToLocalDateTime() {

      def localDateTime = JavaTimeUtil.sqlTimestampToLocalDateTime(sqlTimestamp)

      expect:
      localDateTime.year == year
      localDateTime.month == month
      localDateTime.dayOfMonth == dayOfMonth
      localDateTime.hour == hour
      localDateTime.minute == minute
      localDateTime.second == second

      where:
      sqlTimestamp                          | year | month       | dayOfMonth | hour | minute | second
      makeSqlTimestamp(2013, 3, 17, 0, 0, 0)    | 2013 | Month.MARCH | 17         | 0    | 0      | 0
      makeSqlTimestamp(2013, 3, 17, 8, 0, 0)    | 2013 | Month.MARCH | 17         | 8    | 0      | 0
      makeSqlTimestamp(2013, 3, 17, 16, 47, 0)  | 2013 | Month.MARCH | 17         | 16   | 47     | 0
      makeSqlTimestamp(2013, 3, 17, 18, 30, 45) | 2013 | Month.MARCH | 17         | 18   | 30     | 45
   }

   void testLocalDateToJavaDate() {

      expect:
      JavaTimeUtil.localDateToJavaDate(localDate).getTime() == javaDate.getTime()

      where:
      localDate                            | javaDate
      LocalDate.of(1970, Month.JANUARY, 1) | makeDate(1970, 1, 1, 0, 0, 0)
      LocalDate.of(2013, Month.MARCH, 17)  | makeDate(2013, 3, 17, 0, 0, 0)
   }

   void testLocalDateToSqlDate() {

      expect:
      JavaTimeUtil.localDateToSqlDate(localDate).getTime() == sqlDate.getTime()

      where:
      localDate                            | sqlDate
      LocalDate.of(1970, Month.JANUARY, 1) | makeSqlDate(1970, 1, 1)
      LocalDate.of(2013, Month.MARCH, 17)  | makeSqlDate(2013, 3, 17)
      LocalDate.of(2014, Month.JANUARY, 1) | makeSqlDate(2014, 1, 1)
   }

   void testLocalTimeToSqlTime() {

      expect:
      JavaTimeUtil.localTimeToSqlTime(localTime).getTime() == sqlTime.getTime()

      where:
      localTime                | sqlTime
      LocalTime.of(0, 0, 0)    | makeSqlTime(0, 0, 0)
      LocalTime.of(8, 0, 0)    | makeSqlTime(8, 0, 0)
      LocalTime.of(16, 47, 0)  | makeSqlTime(16, 47, 0)
      LocalTime.of(18, 30, 45) | makeSqlTime(18, 30, 45)
   }

   void testLocalDateTimeToDate() {

      expect:
      JavaTimeUtil.localDateTimeToJavaDate(localDateTime).getTime() == javaDate.getTime()

      where:
      localDateTime                                       | javaDate
      LocalDateTime.of(2013, Month.MARCH, 17, 0, 0, 0)    | makeDate(2013, 3, 17, 0, 0, 0)
      LocalDateTime.of(2013, Month.MARCH, 17, 8, 0, 0)    | makeDate(2013, 3, 17, 8, 0, 0)
      LocalDateTime.of(2013, Month.MARCH, 17, 16, 47, 0)  | makeDate(2013, 3, 17, 16, 47, 0)
      LocalDateTime.of(2013, Month.MARCH, 17, 18, 30, 45) | makeDate(2013, 3, 17, 18, 30, 45)
   }

   void testLocalDateTimeToSqlTimestamp() {

      expect:
      JavaTimeUtil.localDateTimeToSqlTimestamp(localDateTime).getTime() == sqlTimestamp.getTime()

      where:
      localDateTime                                       | sqlTimestamp
      LocalDateTime.of(2013, Month.MARCH, 17, 0, 0, 0)    | makeSqlTimestamp(2013, 3, 17, 0, 0, 0)
      LocalDateTime.of(2013, Month.MARCH, 17, 8, 0, 0)    | makeSqlTimestamp(2013, 3, 17, 8, 0, 0)
      LocalDateTime.of(2013, Month.MARCH, 17, 16, 47, 0)  | makeSqlTimestamp(2013, 3, 17, 16, 47, 0)
      LocalDateTime.of(2013, Month.MARCH, 17, 18, 30, 45) | makeSqlTimestamp(2013, 3, 17, 18, 30, 45)
   }

   void testUsDayNumberToDayOfWeek() {

      expect:
      JavaTimeUtil.usDayNumberToDayOfWeek(1) == DayOfWeek.SUNDAY
      JavaTimeUtil.usDayNumberToDayOfWeek(2) == DayOfWeek.MONDAY
      JavaTimeUtil.usDayNumberToDayOfWeek(3) == DayOfWeek.TUESDAY
      JavaTimeUtil.usDayNumberToDayOfWeek(4) == DayOfWeek.WEDNESDAY
      JavaTimeUtil.usDayNumberToDayOfWeek(5) == DayOfWeek.THURSDAY
      JavaTimeUtil.usDayNumberToDayOfWeek(6) == DayOfWeek.FRIDAY
      JavaTimeUtil.usDayNumberToDayOfWeek(7) == DayOfWeek.SATURDAY
   }

   void testUsDayNumberToDayOfWeekInvalidNumber() {

      when:
      JavaTimeUtil.usDayNumberToDayOfWeek(8)

      then:
      thrown IllegalArgumentException
   }

   void testDayOfWeekToUsDayNumber() {

      expect:
      JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.SUNDAY) == 1
      JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.MONDAY) == 2
      JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.TUESDAY) == 3
      JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.WEDNESDAY) == 4
      JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.THURSDAY) == 5
      JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.FRIDAY) == 6
      JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.SATURDAY) == 7
   }

   void testGetUTCCalender() {

      expect:
      JavaTimeUtil.getUTCCalender().getTimeZone() == TimeZone.getTimeZone("UTC")
   }

   def "toLocalDateTimeAtZone"() {

      def utcDateTime = LocalDateTime.of(1993, 3, 26, 8, 30, 45)
      def nyDateTime = LocalDateTime.of(1993, 3, 26, 3, 30, 45)

      expect:
      JavaTimeUtil.toLocalDateTimeAtZone(utcDateTime, ZoneId.of("UTC"), ZoneId.of("America/New_York")) == nyDateTime
   }

   void testPrivateConstructorSoCoverageIs100Percent() {

      expect:
      new JavaTimeUtil() != null
   }

   private static Date makeDate(int year, int month, int day, int hour, int minute, int second) {

      return new Date(Date.UTC(year - 1900, month - 1, day, hour, minute, second))
   }

   private static java.sql.Date makeSqlDate(int year, int month, int day) {

      return new java.sql.Date(makeDate(year, month, day, 0, 0, 0).getTime())
   }

   private static Time makeSqlTime(int hour, int minute, int second) {

      return new Time(makeDate(1970, 1, 1, hour, minute, second).getTime())
   }

   private static Timestamp makeSqlTimestamp(int year, int month, int day, int hour, int minute, int second) {

      return new Timestamp(makeDate(year, month, day, hour, minute, second).getTime())
   }
}
