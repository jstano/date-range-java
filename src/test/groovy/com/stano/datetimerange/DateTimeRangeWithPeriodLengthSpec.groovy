package com.stano.datetimerange

import spock.lang.Specification

import java.time.LocalDateTime

class DateTimeRangeWithPeriodLengthSpec extends Specification {
  void testGetNumberOfPeriodsInRange() {
    expect:
    DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, periodLength).numberOfPeriodsInRange == expected

    where:
    // exact multiples within same day (8 hours = 480 minutes)
    startDateTime                   | endDateTime                     | periodLength || expected
    localDateTime(2013,1,1,8,0)     | localDateTime(2013,1,1,16,0)    | 60           || 8
    localDateTime(2013,1,1,8,0)     | localDateTime(2013,1,1,16,0)    | 30           || 16
    localDateTime(2013,1,1,8,0)     | localDateTime(2013,1,1,16,0)    | 15           || 32
    localDateTime(2013,1,1,8,0)     | localDateTime(2013,1,1,16,0)    | 10           || 48
    localDateTime(2013,1,1,8,0)     | localDateTime(2013,1,1,16,0)    | 5            || 96
    localDateTime(2013,1,1,8,0)     | localDateTime(2013,1,1,16,0)    | 1            || 480

    // crossing midnight (also 8 hours)
    localDateTime(2013,1,1,22,0)    | localDateTime(2013,1,2,6,0)     | 60           || 8
    localDateTime(2013,1,1,22,0)    | localDateTime(2013,1,2,6,0)     | 30           || 16
    localDateTime(2013,1,1,22,0)    | localDateTime(2013,1,2,6,0)     | 15           || 32
    localDateTime(2013,1,1,22,0)    | localDateTime(2013,1,2,6,0)     | 10           || 48
    localDateTime(2013,1,1,22,0)    | localDateTime(2013,1,2,6,0)     | 5            || 96
    localDateTime(2013,1,1,22,0)    | localDateTime(2013,1,2,6,0)     | 1            || 480

    // non-exact multiple (70 minutes / 15 = 4 periods)
    localDateTime(2013,1,1,8,0)     | localDateTime(2013,1,1,9,10)    | 15           || 4

    // shorter than one period -> 0
    localDateTime(2013,1,1,8,0)     | localDateTime(2013,1,1,8,10)    | 15           || 0
  }
  void testGetters() {
    def startDateTime = localDateTime(2013, 1, 1, 8, 0)
    def endDateTime = localDateTime(2013, 1, 1, 16, 0)
    def periodLength = 60
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)
    def dateTimeRangeWithPeriod = DateTimeRangeWithPeriodLength.of(dateTimeRange, periodLength)

    expect:
    dateTimeRangeWithPeriod.dateTimeRange == dateTimeRange
    dateTimeRangeWithPeriod.periodLengthInMinutes == 60
  }

  void testGetStartingAndEndingIndex() {
    def dateTimeRange = DateTimeRange.of(startDateTime, endDateTime)
    def dateTimeRangeWithPeriod = DateTimeRangeWithPeriodLength.of(dateTimeRange, periodLength)

    expect:
    dateTimeRangeWithPeriod.startIndex == expectedStartIndex
    dateTimeRangeWithPeriod.endIndex == expectedEndIndex

    where:
    startDateTime                    | endDateTime                      | periodLength | expectedStartIndex | expectedEndIndex
    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 60           | 8                  | 16
    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 30           | 16                 | 32
    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 15           | 32                 | 64
    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 10           | 48                 | 96
    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 5            | 96                 | 192
    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 1            | 480                | 960

    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 60           | 22                 | 30
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 30           | 44                 | 60
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 15           | 88                 | 120
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 10           | 132                | 180
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 5            | 264                | 360
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 1            | 1320               | 1800
  }

  void testEquals() {

    def dateTimeRange1 = DateTimeRangeWithPeriodLength.of(localDateTime(2012, 1, 1, 8, 17), localDateTime(2012, 1, 7, 8, 17), 30)
    def dateTimeRange2 = DateTimeRangeWithPeriodLength.of(localDateTime(2012, 1, 1, 8, 17), localDateTime(2012, 1, 7, 8, 17), 30)
    def dateTimeRange3 = DateTimeRangeWithPeriodLength.of(localDateTime(2012, 1, 1, 8, 17), localDateTime(2012, 1, 8, 8, 17), 30)
    def dateTimeRange4 = DateTimeRangeWithPeriodLength.of(localDateTime(2012, 1, 2, 8, 17), localDateTime(2012, 1, 7, 8, 17), 30)
    def dateTimeRange5 = DateTimeRangeWithPeriodLength.of(localDateTime(2012, 1, 4, 8, 17), localDateTime(2012, 1, 6, 8, 17), 30)

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
    def startDateTime = localDateTime(2013, 1, 1, 8, 17)
    def endDateTime = localDateTime(2013, 2, 1, 17, 53)
    def dateTimeRange = DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, 30)

    int expectedHashCode = 31 * startDateTime.hashCode() + endDateTime.hashCode()

    expect:
    dateTimeRange.hashCode() == expectedHashCode
  }

  void testIterator() {
    def startDateTime = localDateTime(2013, 1, 1, 8, 0)
    def endDateTime = localDateTime(2013, 1, 1, 16, 0)
    def dateTimeRange = DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, 30)

    def expected = [
      localDateTime(2013, 1, 1, 8, 0),
      localDateTime(2013, 1, 1, 8, 30),
      localDateTime(2013, 1, 1, 9, 0),
      localDateTime(2013, 1, 1, 9, 30),
      localDateTime(2013, 1, 1, 10, 0),
      localDateTime(2013, 1, 1, 10, 30),
      localDateTime(2013, 1, 1, 11, 0),
      localDateTime(2013, 1, 1, 11, 30),
      localDateTime(2013, 1, 1, 12, 0),
      localDateTime(2013, 1, 1, 12, 30),
      localDateTime(2013, 1, 1, 13, 0),
      localDateTime(2013, 1, 1, 13, 30),
      localDateTime(2013, 1, 1, 14, 0),
      localDateTime(2013, 1, 1, 14, 30),
      localDateTime(2013, 1, 1, 15, 0),
      localDateTime(2013, 1, 1, 15, 30),
    ]

    def results = new ArrayList<LocalDateTime>();

    for (LocalDateTime dateTime : dateTimeRange) {
      results.add(dateTime);
    }

    expect:
    expected == results
  }

  void testIteratorRemove() {
    def startDateTime = localDateTime(2013, 1, 1, 8, 0)
    def endDateTime = localDateTime(2013, 1, 1, 16, 0)
    def dateTimeRange = DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, 30)

    when:
    dateTimeRange.iterator().remove()

    then:
    thrown UnsupportedOperationException
  }

  void testGetIndexRange() {
    def dateTimeRange = DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, periodLength)
    def indexStream = dateTimeRange.getIndexStream()
    def indexes = indexStream.boxed().toList()

    expect:
    indexes.get(0) == startIndex
    indexes.get(indexes.size() - 1) == endIndex

    where:
    startDateTime                    | endDateTime                      | periodLength | startIndex | endIndex
    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 60           | 8          | 15
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 60           | 22         | 29

    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 30           | 16         | 31
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 30           | 44         | 59

    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 15           | 32         | 63
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 15           | 88         | 119

    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 10           | 48         | 95
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 10           | 132        | 179

    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 5            | 96         | 191
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 5            | 264        | 359

    localDateTime(2013, 1, 1, 8, 0)  | localDateTime(2013, 1, 1, 16, 0) | 1            | 480        | 959
    localDateTime(2013, 1, 1, 22, 0) | localDateTime(2013, 1, 2, 6, 0)  | 1            | 1320       | 1799
  }

  static LocalDateTime localDateTime(int year, int month, int day, int hour, int minute) {
    return LocalDateTime.of(year, month, day, hour, minute)
  }
}
