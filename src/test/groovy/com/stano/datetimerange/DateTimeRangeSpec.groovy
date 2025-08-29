package com.stano.datetimerange

import com.stano.timerange.TimeRange
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DateTimeRangeSpec extends Specification {
  void testWithDateTimes() {
    def startDateTime = LocalDateTime.of(2013, 1, 1, 8, 17)
    def endDateTime = LocalDateTime.of(2013, 2, 1, 17, 53)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)

    expect:
    dateTimeRange.startDateTime == startDateTime
    dateTimeRange.endDateTime == endDateTime
  }

  void testFromTimeRangeOnDate() {
    def startTime = LocalTime.of(8, 17, 45)
    def endTime = LocalTime.of(17, 53, 22)
    def timeRange = TimeRange.of(startTime, endTime)
    def dateTimeRange = DateTimeRange.fromTimeRangeOnDate(timeRange, LocalDate.of(2013, 1, 7))

    expect:
    dateTimeRange.startDateTime == LocalDateTime.of(2013, 1, 7, 8, 17, 45)
    dateTimeRange.endDateTime == LocalDateTime.of(2013, 1, 7, 17, 53, 22)
  }

  void testFromTimeRangeOnDateOverMidnight() {
    def startTime = LocalTime.of(22, 0, 0)
    def endTime = LocalTime.of(6, 30, 0)
    def timeRange = TimeRange.of(startTime, endTime)
    def dateTimeRange = DateTimeRange.fromTimeRangeOnDate(timeRange, LocalDate.of(2013, 1, 7))

    expect:
    dateTimeRange.startDateTime == LocalDateTime.of(2013, 1, 7, 22, 0, 0)
    dateTimeRange.endDateTime == LocalDateTime.of(2013, 1, 8, 6, 30, 0)
  }

  void testAllDay() {
    def dateTimeRange = DateTimeRange.allDay(LocalDate.of(2013, 1, 7))

    expect:
    dateTimeRange.startDateTime == LocalDateTime.of(2013, 1, 7, 0, 0, 0)
    dateTimeRange.endDateTime == LocalDateTime.of(2013, 1, 8, 0, 0, 0)
  }

  void testGetDuration() {
    def startDateTime = LocalDateTime.of(2013, 1, 1, 8, 0)
    def endDateTime = LocalDateTime.of(2013, 1, 1, 16, 0)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)

    expect:
    dateTimeRange.duration.toMillis() == 28800000
  }

  void testOverlaps() {
    def startDateTime = LocalDateTime.of(2012, 1, 8, 0, 0, 0)
    def endDateTime = LocalDateTime.of(2012, 1, 14, 0, 0, 0)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)
    def dateTimeRangeBefore = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 7, 0, 0, 0))
    def dateTimeRangeAfter = DateTimeRange.of(LocalDateTime.of(2012, 1, 15, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0))
    def dateTimeRangeOverlapStart = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 8, 0, 0, 0))
    def dateTimeRangeOverlapEnd = DateTimeRange.of(LocalDateTime.of(2012, 1, 14, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0))
    def dateTimeRangeOverlapStart2 = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 9, 0, 0, 0))
    def dateTimeRangeOverlapEnd2 = DateTimeRange.of(LocalDateTime.of(2012, 1, 13, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0))

    expect:
    dateTimeRange.overlaps(dateTimeRange) == true
    dateTimeRange.overlaps(dateTimeRangeBefore) == false
    dateTimeRange.overlaps(dateTimeRangeAfter) == false
    dateTimeRange.overlaps(dateTimeRangeOverlapStart) == true
    dateTimeRange.overlaps(dateTimeRangeOverlapEnd) == true
    dateTimeRange.overlaps(dateTimeRangeOverlapStart2) == true
    dateTimeRange.overlaps(dateTimeRangeOverlapEnd2) == true
    dateTimeRange.overlaps(null) == false
  }

  void testOverlapsExclusive() {
    def startDateTime = LocalDateTime.of(2012, 1, 8, 0, 0, 0)
    def endDateTime = LocalDateTime.of(2012, 1, 14, 0, 0, 0)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)
    def dateTimeRangeBefore = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 7, 0, 0, 0))
    def dateTimeRangeAfter = DateTimeRange.of(LocalDateTime.of(2012, 1, 15, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0))
    def dateTimeRangeOverlapStart = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 8, 0, 0, 0))
    def dateTimeRangeOverlapEnd = DateTimeRange.of(LocalDateTime.of(2012, 1, 14, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0))
    def dateTimeRangeOverlapStart2 = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 9, 0, 0, 0))
    def dateTimeRangeOverlapEnd2 = DateTimeRange.of(LocalDateTime.of(2012, 1, 13, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0))

    expect:
    dateTimeRange.overlapsExclusive(dateTimeRange) == true
    dateTimeRange.overlapsExclusive(dateTimeRangeBefore) == false
    dateTimeRange.overlapsExclusive(dateTimeRangeAfter) == false
    dateTimeRange.overlapsExclusive(dateTimeRangeOverlapStart) == false
    dateTimeRange.overlapsExclusive(dateTimeRangeOverlapEnd) == false
    dateTimeRange.overlapsExclusive(dateTimeRangeOverlapStart2) == true
    dateTimeRange.overlapsExclusive(dateTimeRangeOverlapEnd2) == true
    dateTimeRange.overlapsExclusive(null) == false
  }

  def "checks for a dateTimeRange being completely contained within this dateTime range"() {
    given: "an existing DateTime Range"
    def dateTimeRange = DateTimeRange.of(LocalDateTime.of(2014, 10, 1, 9, 0), LocalDateTime.of(2014, 10, 2, 17, 0))

    expect:
    expectedResult == dateTimeRange.overlapsCompletely(dateTimeRangeToCheck)

    where:
    dateTimeRangeToCheck                                                                         | expectedResult
    DateTimeRange.of(LocalDateTime.of(2014, 9, 30, 17, 0), LocalDateTime.of(2014, 10, 1, 8, 59)) | false
    DateTimeRange.of(LocalDateTime.of(2014, 9, 30, 17, 0), LocalDateTime.of(2014, 10, 1, 9, 0))  | false
    DateTimeRange.of(LocalDateTime.of(2014, 9, 30, 17, 0), LocalDateTime.of(2014, 10, 1, 9, 1))  | false
    DateTimeRange.of(LocalDateTime.of(2014, 10, 1, 9, 0), LocalDateTime.of(2014, 10, 1, 9, 1))   | true
    DateTimeRange.of(LocalDateTime.of(2014, 10, 1, 9, 1), LocalDateTime.of(2014, 10, 2, 16, 59)) | true
    DateTimeRange.of(LocalDateTime.of(2014, 10, 1, 9, 0), LocalDateTime.of(2014, 10, 2, 17, 0))  | true
    DateTimeRange.of(LocalDateTime.of(2014, 10, 1, 9, 0), LocalDateTime.of(2014, 10, 2, 17, 1))  | false
    DateTimeRange.of(LocalDateTime.of(2014, 10, 2, 17, 1), LocalDateTime.of(2014, 10, 2, 17, 2)) | false
  }

  void testContainsDateTime() {
    def startDateTime = LocalDateTime.of(2012, 1, 8, 8, 0, 0)
    def endDateTime = LocalDateTime.of(2012, 1, 14, 16, 0, 0)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)

    def dateTimeBefore1 = LocalDateTime.of(2012, 1, 7, 0, 0, 0)
    def dateTimeBefore2 = LocalDateTime.of(2012, 1, 8, 7, 59, 59)
    def dateTimeAfter1 = LocalDateTime.of(2012, 1, 15, 0, 0, 0)
    def dateTimeAfter2 = LocalDateTime.of(2012, 1, 14, 16, 1, 0)

    def dateTimeInside1 = LocalDateTime.of(2012, 1, 8, 8, 0, 0)
    def dateTimeInside2 = LocalDateTime.of(2012, 1, 9, 16, 0, 0)
    def dateTimeInside3 = LocalDateTime.of(2012, 1, 14, 16, 0, 0)

    expect:
    dateTimeRange.containsDateTime(null) == false
    dateTimeRange.containsDateTime(dateTimeBefore1) == false
    dateTimeRange.containsDateTime(dateTimeBefore2) == false
    dateTimeRange.containsDateTime(dateTimeAfter1) == false
    dateTimeRange.containsDateTime(dateTimeAfter2) == false

    dateTimeRange.containsDateTime(dateTimeInside1) == true
    dateTimeRange.containsDateTime(dateTimeInside2) == true
    dateTimeRange.containsDateTime(dateTimeInside3) == true
  }

  void testCompareTo() {
    def dateTimeRange1 = DateTimeRange.of(LocalDateTime.of(2012, 1, 8, 8, 17, 0), LocalDateTime.of(2012, 1, 14, 8, 17, 0))
    def dateTimeRange2 = DateTimeRange.of(LocalDateTime.of(2012, 1, 8, 8, 17, 0), LocalDateTime.of(2012, 1, 14, 8, 17, 0))
    def dateTimeRange3 = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 8, 17, 0), LocalDateTime.of(2012, 1, 7, 8, 17, 0))
    def dateTimeRange4 = DateTimeRange.of(LocalDateTime.of(2012, 1, 15, 8, 17, 0), LocalDateTime.of(2012, 1, 21, 8, 17, 0))
    def dateTimeRange5 = DateTimeRange.of(LocalDateTime.of(2012, 1, 8, 8, 17, 0), LocalDateTime.of(2012, 1, 13, 8, 17, 0))
    def dateTimeRange6 = DateTimeRange.of(LocalDateTime.of(2012, 1, 8, 8, 17, 0), LocalDateTime.of(2012, 1, 15, 8, 17, 0))

    expect:
    dateTimeRange1.compareTo(dateTimeRange1) == 0
    dateTimeRange1.compareTo(dateTimeRange2) == 0
    dateTimeRange1.compareTo(null) == -1
    dateTimeRange1.compareTo(dateTimeRange3) >= 1
    dateTimeRange1.compareTo(dateTimeRange4) < 0
    dateTimeRange1.compareTo(dateTimeRange5) > 0
    dateTimeRange1.compareTo(dateTimeRange6) < 0
  }

  void testEquals() {
    def dateTimeRange1 = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 8, 17, 0), LocalDateTime.of(2012, 1, 7, 8, 17, 0))
    def dateTimeRange2 = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 8, 17, 0), LocalDateTime.of(2012, 1, 7, 8, 17, 0))
    def dateTimeRange3 = DateTimeRange.of(LocalDateTime.of(2012, 1, 1, 8, 17, 0), LocalDateTime.of(2012, 1, 8, 8, 17, 0))
    def dateTimeRange4 = DateTimeRange.of(LocalDateTime.of(2012, 1, 2, 8, 17, 0), LocalDateTime.of(2012, 1, 7, 8, 17, 0))
    def dateTimeRange5 = DateTimeRange.of(LocalDateTime.of(2012, 1, 4, 8, 17, 0), LocalDateTime.of(2012, 1, 6, 8, 17, 0))

    expect:
    dateTimeRange1.equals(null) == false
    dateTimeRange1.equals("ABC") == false
    dateTimeRange1.equals(dateTimeRange1) == true
    dateTimeRange1.equals(dateTimeRange2) == true
    dateTimeRange1.equals(dateTimeRange3) == false
    dateTimeRange1.equals(dateTimeRange4) == false
    dateTimeRange1.equals(dateTimeRange5) == false
  }

  void testHashCode() {
    def startDateTime = LocalDateTime.of(2013, 1, 1, 8, 17)
    def endDateTime = LocalDateTime.of(2013, 2, 1, 17, 53)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)

    int expectedHashCode = 31 * startDateTime.hashCode() + endDateTime.hashCode()

    expect:
    dateTimeRange.hashCode() == expectedHashCode
  }

  def "containsExclusive should not include end points"() {
    def startDateTime = LocalDateTime.of(2013, 1, 1, 8, 17)
    def endDateTime = LocalDateTime.of(2013, 2, 1, 17, 53)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)

    expect:
    isContainedExclusive == dateTimeRange.containsDateTimeExclusive(dateTime)

    where:
    dateTime                | isContainedExclusive
    null                    | false
    ldt(2013, 1, 1, 8, 17)  | false
    ldt(2013, 1, 1, 8, 18)  | true
    ldt(2013, 2, 1, 17, 52) | true
    ldt(2013, 2, 1, 17, 53) | false
  }

  def "containsDateTimeExclusiveOfEndDateTime includes start and excludes end"() {
    def startDateTime = LocalDateTime.of(2013, 1, 1, 8, 17)
    def endDateTime = LocalDateTime.of(2013, 2, 1, 17, 53)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)

    expect:
    isContained == dateTimeRange.containsDateTimeExclusiveOfEndDateTime(dateTime)

    where:
    dateTime                  | isContained
    null                      | false
    ldt(2012, 12, 31, 23, 59) | false
    ldt(2013, 1, 1, 8, 16)    | false
    ldt(2013, 1, 1, 8, 17)    | true   // start inclusive
    ldt(2013, 1, 1, 8, 18)    | true
    ldt(2013, 2, 1, 17, 52)   | true
    ldt(2013, 2, 1, 17, 53)   | false  // end exclusive
    ldt(2013, 2, 1, 17, 54)   | false
  }

  def "getFractionalHours computes expected fractional hours"() {
    expect:
    Math.abs(DateTimeRange.of(start, end).fractionalHours - expected) < 1e-9

    where:
    // exact hours same day
    start                                  | end                                     || expected
    LocalDateTime.of(2013, 1, 1, 8, 0, 0)  | LocalDateTime.of(2013, 1, 1, 16, 0, 0)  || 8.0d
    // crossing midnight (8 hours)
    LocalDateTime.of(2013, 1, 1, 22, 0, 0) | LocalDateTime.of(2013, 1, 2, 6, 0, 0)   || 8.0d
    // partial with minutes (90 min)
    LocalDateTime.of(2013, 1, 1, 8, 15, 0) | LocalDateTime.of(2013, 1, 1, 9, 45, 0)  || 1.5d
    // second-level precision (1 hour exactly)
    LocalDateTime.of(2013, 1, 1, 8, 0, 30) | LocalDateTime.of(2013, 1, 1, 9, 0, 30)  || 1.0d
    // 30 minutes 30 seconds -> 0.508333...
    LocalDateTime.of(2013, 1, 1, 8, 0, 0)  | LocalDateTime.of(2013, 1, 1, 8, 30, 30) || (30 * 60 + 30) / 3600.0d
    // zero duration
    LocalDateTime.of(2013, 1, 1, 8, 0, 0)  | LocalDateTime.of(2013, 1, 1, 8, 0, 0)   || 0.0d
  }

  def "overlap of two DateTimeRanges should have a DateTimeRange itself"() {
    def startDateTime = LocalDateTime.of(2014, 9, 23, 9, 25)
    def endDateTime = LocalDateTime.of(2014, 9, 23, 17, 45)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)

    expect:
    overlappingRange == dateTimeRange.overlapRange(otherDateTimeRange)

    where:
    otherDateTimeRange                                                   | overlappingRange
    null                                                                 | null
    DateTimeRange.of(ldt(2014, 9, 23, 9, 00), ldt(2014, 9, 23, 9, 24))   | null
    DateTimeRange.of(ldt(2014, 9, 23, 9, 00), ldt(2014, 9, 23, 9, 25))   | DateTimeRange.of(ldt(2014, 9, 23, 9, 25), ldt(2014, 9, 23, 9, 25))
    DateTimeRange.of(ldt(2014, 9, 23, 9, 20), ldt(2014, 9, 23, 10, 00))  | DateTimeRange.of(ldt(2014, 9, 23, 9, 25), ldt(2014, 9, 23, 10, 00))
    DateTimeRange.of(ldt(2014, 9, 23, 12, 00), ldt(2014, 9, 23, 13, 00)) | DateTimeRange.of(ldt(2014, 9, 23, 12, 00), ldt(2014, 9, 23, 13, 00))
    DateTimeRange.of(ldt(2014, 9, 23, 17, 40), ldt(2014, 9, 23, 17, 50)) | DateTimeRange.of(ldt(2014, 9, 23, 17, 40), ldt(2014, 9, 23, 17, 45))
    DateTimeRange.of(ldt(2014, 9, 23, 9, 00), ldt(2014, 9, 23, 18, 00))  | DateTimeRange.of(ldt(2014, 9, 23, 9, 25), ldt(2014, 9, 23, 17, 45))
  }

  def "overlap of two DateTimeRanges should have some duration"() {
    def startDateTime = LocalDateTime.of(2014, 9, 23, 9, 25)
    def endDateTime = LocalDateTime.of(2014, 9, 23, 17, 45)
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)

    expect:
    duration == dateTimeRange.overlapDuration(otherDateTimeRange)

    where:
    otherDateTimeRange                                                   | duration
    null                                                                 | Duration.ofMinutes(0)
    DateTimeRange.of(ldt(2014, 9, 23, 9, 00), ldt(2014, 9, 23, 9, 25))   | Duration.ofMinutes(0)
    DateTimeRange.of(ldt(2014, 9, 23, 9, 20), ldt(2014, 9, 23, 10, 00))  | Duration.ofMinutes(35)
    DateTimeRange.of(ldt(2014, 9, 23, 11, 00), ldt(2014, 9, 23, 12, 00)) | Duration.ofHours(1)
    DateTimeRange.of(ldt(2014, 9, 23, 17, 44), ldt(2014, 9, 23, 17, 46)) | Duration.ofMinutes(1)
    DateTimeRange.of(ldt(2014, 9, 23, 9, 00), ldt(2014, 9, 23, 18, 00))  | Duration.ofMinutes(500)
  }

  private static LocalDateTime ldt(int year, int month, int day, int hour, int minute) {
    return LocalDateTime.of(year, month, day, hour, minute)
  }
}
