package com.stano.datetime

import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class DTUtilSpec extends Specification {

   def "minimum of two times, null iff both are null"() {

      expect:
      earliestTime == DTUtil.earliest(time1, time2)

      where:
      time1                    | time2                   | earliestTime
      null                     | null                    | null
      ldt(2014, 9, 23, 10, 00) | null                    | ldt(2014, 9, 23, 10, 00)
      null                     | ldt(2014, 9, 23, 9, 00) | ldt(2014, 9, 23, 9, 00)
      ldt(2014, 9, 23, 9, 00)  | ldt(2014, 9, 23, 9, 00) | ldt(2014, 9, 23, 9, 00)
      ldt(2014, 9, 23, 10, 00) | ldt(2014, 9, 23, 9, 00) | ldt(2014, 9, 23, 9, 00)
   }

   def "maximum of two times, null if both are null"() {

      expect:
      latestTime == DTUtil.latest(time1, time2)

      where:
      time1                    | time2                   | latestTime
      null                     | null                    | null
      ldt(2014, 9, 23, 10, 00) | null                    | ldt(2014, 9, 23, 10, 00)
      null                     | ldt(2014, 9, 23, 9, 00) | ldt(2014, 9, 23, 9, 00)
      ldt(2014, 9, 23, 9, 00)  | ldt(2014, 9, 23, 9, 00) | ldt(2014, 9, 23, 9, 00)
      ldt(2014, 9, 23, 10, 00) | ldt(2014, 9, 23, 9, 00) | ldt(2014, 9, 23, 10, 00)
   }

   def "durationInHours should work"() {

      expect:
      DTUtil.durationInHours(startDateTime, endDateTime) == expectedResult

      where:
      startDateTime           | endDateTime             | expectedResult
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 16, 0)  | 8
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 16, 30) | 8
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 17, 0)  | 9
      ldt(2015, 2, 7, 0, 0)   | ldt(2015, 2, 8, 0, 0)   | 24
      ldt(2015, 2, 7, 0, 0)   | ldt(2015, 2, 9, 0, 0)   | 48
      ldt(2015, 2, 7, 16, 0)  | ldt(2015, 2, 7, 8, 0)   | -8
      ldt(2015, 2, 7, 16, 30) | ldt(2015, 2, 7, 8, 0)   | -8
      ldt(2015, 2, 7, 17, 0)  | ldt(2015, 2, 7, 8, 0)   | -9
   }

   def "durationInMinutes should work"() {

      expect:
      DTUtil.durationInMinutes(startDateTime, endDateTime) == expectedResult

      where:
      startDateTime           | endDateTime             | expectedResult
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 16, 0)  | 480
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 16, 30) | 510
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 17, 0)  | 540
      ldt(2015, 2, 7, 0, 0)   | ldt(2015, 2, 8, 0, 0)   | 1_440
      ldt(2015, 2, 7, 0, 0)   | ldt(2015, 2, 9, 0, 0)   | 2_880
      ldt(2015, 2, 7, 16, 0)  | ldt(2015, 2, 7, 8, 0)   | -480
      ldt(2015, 2, 7, 16, 30) | ldt(2015, 2, 7, 8, 0)   | -510
      ldt(2015, 2, 7, 17, 0)  | ldt(2015, 2, 7, 8, 0)   | -540
   }

   def "durationInSeconds should work"() {

      expect:
      DTUtil.durationInSeconds(startDateTime, endDateTime) == expectedResult

      where:
      startDateTime           | endDateTime             | expectedResult
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 16, 0)  | 28_800
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 16, 30) | 30_600
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 17, 0)  | 32_400
      ldt(2015, 2, 7, 0, 0)   | ldt(2015, 2, 8, 0, 0)   | 86_400
      ldt(2015, 2, 7, 0, 0)   | ldt(2015, 2, 9, 0, 0)   | 172_800
      ldt(2015, 2, 7, 16, 0)  | ldt(2015, 2, 7, 8, 0)   | -28_800
      ldt(2015, 2, 7, 16, 30) | ldt(2015, 2, 7, 8, 0)   | -30_600
      ldt(2015, 2, 7, 17, 0)  | ldt(2015, 2, 7, 8, 0)   | -32_400
   }

   def "durationInFractionalSeconds should work"() {

      expect:
      DTUtil.durationInFractionalSeconds(startDateTime, endDateTime).compareTo(expectedResult) == 0

      where:
      startDateTime | endDateTime | expectedResult
      millis(0)     | millis(0)   | new BigDecimal("0")
      millis(0)     | millis(1)   | new BigDecimal("0.001")
      millis(0)     | millis(10)  | new BigDecimal("0.01")
      millis(0)     | millis(100) | new BigDecimal("0.1")
      millis(0)     | seconds(1)  | new BigDecimal("1")
   }

   def "durationInFractionalHours should work"() {

      expect:
      DTUtil.durationInFractionalHours(startDateTime, endDateTime) == expectedResult

      where:
      startDateTime           | endDateTime             | expectedResult
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 16, 0)  | 8.0
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 16, 30) | 8.5
      ldt(2015, 2, 7, 8, 0)   | ldt(2015, 2, 7, 17, 0)  | 9.0
      ldt(2015, 2, 7, 0, 0)   | ldt(2015, 2, 8, 0, 0)   | 24.0
      ldt(2015, 2, 7, 0, 0)   | ldt(2015, 2, 9, 0, 0)   | 48.0
      ldt(2015, 2, 7, 16, 0)  | ldt(2015, 2, 7, 8, 0)   | -8.0
      ldt(2015, 2, 7, 16, 30) | ldt(2015, 2, 7, 8, 0)   | -8.5
      ldt(2015, 2, 7, 17, 0)  | ldt(2015, 2, 7, 8, 0)   | -9.0
   }

   def "call the private constructor to get 100% coverage"() {

      expect:
      new DTUtil() != null
   }

   private static LocalDateTime ldt(int year, int month, int day, int hour, int minute) {

      return LocalDate.of(year, month, day).atTime(hour, minute, 0);
   }

   private static LocalDateTime seconds(int seconds) {

      return LocalDateTime.of(2018, 10, 6, 1, 0, seconds);
   }

   private static LocalDateTime millis(int millis) {

      return LocalDateTime.of(2018, 10, 6, 1, 0, 0, millis * DateTimeConstants.NANOS_PER_MILLI);
   }
}
