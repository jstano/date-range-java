package com.stano.timerange

import spock.lang.Specification

import java.time.LocalTime

class TimeRangeSpec extends Specification {
  void testWithTimes() {
    def startTime = LocalTime.of(8, 30, 17)
    def endTime = LocalTime.of(17, 45, 38)
    def timeRange = TimeRange.of(startTime, endTime)

    expect:
    timeRange.startTime == startTime
    timeRange.endTime == endTime
  }

  void testGetDuration() {
    def startTime = LocalTime.of(8, 0, 0)
    def endTime = LocalTime.of(16, 0, 0)
    def timeRange = TimeRange.of(startTime, endTime)

    expect:
    timeRange.duration.toMillis() == 28800000
  }

  void testGetDurationNegative() {
    def startTime = LocalTime.of(16, 0, 0)
    def endTime = LocalTime.of(8, 0, 0)
    def timeRange = TimeRange.of(startTime, endTime)

    expect:
    timeRange.duration.toMillis() == -28800000
  }

  void testEquals() {
    def timeRange1 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(17, 45, 38))
    def timeRange2 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(17, 45, 38))
    def timeRange3 = TimeRange.of(LocalTime.of(9, 30, 17), LocalTime.of(17, 45, 38))
    def timeRange4 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(15, 17, 45))
    def timeRange5 = TimeRange.of(LocalTime.of(8, 17, 14), LocalTime.of(12, 18, 54))
    def timeRange6 = TimeRange.of(LocalTime.of(23, 00, 00), LocalTime.MIDNIGHT)
    def timeRange7 = TimeRange.of(LocalTime.of(23, 00, 00), LocalTime.MIDNIGHT)

    expect:
    timeRange1.equals(null) == false
    timeRange1.equals("ABC") == false
    timeRange1.equals(timeRange1) == true
    timeRange1.equals(timeRange2) == true
    timeRange1.equals(timeRange3) == false
    timeRange1.equals(timeRange4) == false
    timeRange1.equals(timeRange5) == false
    timeRange6.equals(timeRange7) == true
  }

  void testHashCode() {
    def startTime = LocalTime.of(8, 17, 48)
    def endTime = LocalTime.of(17, 45, 38)
    def timeRange = TimeRange.of(startTime, endTime)

    int expectedHashCode = 31 * startTime.hashCode() + endTime.hashCode()

    expect:
    timeRange.hashCode() == expectedHashCode
  }

  void testOverlaps() {
    expect:
    overlapsWith == timeRange.overlaps(timeRangeToCheck)

    where:
    timeRange                        | overlapsWith | timeRangeToCheck                  | scenario
    tr(lt(8, 0), lt(16, 0))          | true         | tr(lt(2, 0), lt(22, 0))           | "checked range spans before and after"
    tr(lt(8, 0), lt(16, 0))          | true         | tr(lt(8, 0), lt(16, 0))           | "checked range exactly matches"
    tr(lt(8, 0), lt(16, 0))          | false        | tr(lt(2, 0), lt(4, 0))            | "checked range before"
    tr(lt(8, 0), lt(16, 0))          | true         | tr(lt(2, 0), lt(10, 0))           | "checked range overlaps start"
    tr(lt(8, 0), lt(16, 0))          | true         | tr(lt(10, 0), lt(14, 0))          | "checked range inside"
    tr(lt(8, 0), lt(16, 0))          | true         | tr(lt(10, 0), lt(22, 0))          | "checked range overlaps end"
    tr(lt(8, 0), lt(16, 0))          | false        | tr(lt(17, 0), lt(22, 0))          | "checked range after"
    tr(lt(8, 0), lt(16, 0))          | true         | tr(lt(2, 0), LocalTime.MIDNIGHT)  | "checked range spans before and after with Midnight"
    tr(lt(8, 0), lt(16, 0))          | true         | tr(lt(10, 0), LocalTime.MIDNIGHT) | "checked range spans end with Midnight"
    tr(lt(8, 0), lt(16, 0))          | false        | tr(lt(17, 0), LocalTime.MIDNIGHT) | "checked range is after with Midnight"
    tr(lt(8, 0), LocalTime.MIDNIGHT) | true         | tr(lt(2, 0), LocalTime.MIDNIGHT)  | "range with Midnight end - checked range starts before and ends at Midnight"
    tr(lt(8, 0), LocalTime.MIDNIGHT) | true         | tr(lt(8, 0), LocalTime.MIDNIGHT)  | "range with Midnight end - checked range exactly matches"
    tr(lt(8, 0), LocalTime.MIDNIGHT) | false        | tr(lt(2, 0), lt(4, 0))            | "range with Midnight end - checked range before"
    tr(lt(8, 0), LocalTime.MIDNIGHT) | true         | tr(lt(2, 0), lt(10, 0))           | "range with Midnight end - checked range overlaps start"
    tr(lt(8, 0), LocalTime.MIDNIGHT) | true         | tr(lt(10, 0), lt(14, 0))          | "range with Midnight end - checked range inside"
    tr(lt(8, 0), LocalTime.MIDNIGHT) | true         | tr(lt(10, 0), LocalTime.MIDNIGHT) | "range with Midnight end - checked range starts inside and ends at Midnight"
    tr(lt(8, 0), LocalTime.MIDNIGHT) | false        | null                              | "null"
  }

  void testCompareTo() {
    def timeRange1 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(17, 45, 38))
    def timeRange2 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(17, 45, 38))
    def timeRange3 = TimeRange.of(LocalTime.of(6, 30, 17), LocalTime.of(17, 45, 38))
    def timeRange4 = TimeRange.of(LocalTime.of(9, 30, 17), LocalTime.of(15, 17, 45))
    def timeRange5 = TimeRange.of(LocalTime.of(6, 17, 14), LocalTime.of(12, 18, 54))
    def timeRange6 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(19, 18, 54))

    expect:
    timeRange1.compareTo(timeRange1) == 0
    timeRange1.compareTo(timeRange2) == 0
    timeRange1.compareTo(null) == -1
    timeRange1.compareTo(timeRange3) == 1
    timeRange1.compareTo(timeRange4) == -1
    timeRange1.compareTo(timeRange5) == 1
    timeRange1.compareTo(timeRange6) == -1
  }

  static LocalTime lt(int hour, int minute) {
    return LocalTime.of(hour, minute);
  }

  static TimeRange tr(LocalTime startTime, LocalTime endTime) {
    return TimeRange.of(startTime, endTime);
  }
}
