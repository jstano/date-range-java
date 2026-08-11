package com.stano.datetimerange;

import com.stano.timerange.TimeRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeRangeTest {

  @Test
  void testWithDateTimes() {
    LocalDateTime startDateTime = LocalDateTime.of(2013, 1, 1, 8, 17);
    LocalDateTime endDateTime = LocalDateTime.of(2013, 2, 1, 17, 53);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);

    assertEquals(startDateTime, dateTimeRange.getStartDateTime());
    assertEquals(endDateTime, dateTimeRange.getEndDateTime());
  }

  @Test
  void testFromTimeRangeOnDate() {
    LocalTime startTime = LocalTime.of(8, 17, 45);
    LocalTime endTime = LocalTime.of(17, 53, 22);
    TimeRange timeRange = TimeRange.of(startTime, endTime);
    DateTimeRange dateTimeRange =
        DateTimeRange.fromTimeRangeOnDate(timeRange, LocalDate.of(2013, 1, 7));

    assertEquals(LocalDateTime.of(2013, 1, 7, 8, 17, 45), dateTimeRange.getStartDateTime());
    assertEquals(LocalDateTime.of(2013, 1, 7, 17, 53, 22), dateTimeRange.getEndDateTime());
  }

  @Test
  void testFromTimeRangeOnDateOverMidnight() {
    LocalTime startTime = LocalTime.of(22, 0, 0);
    LocalTime endTime = LocalTime.of(6, 30, 0);
    TimeRange timeRange = TimeRange.of(startTime, endTime);
    DateTimeRange dateTimeRange =
        DateTimeRange.fromTimeRangeOnDate(timeRange, LocalDate.of(2013, 1, 7));

    assertEquals(LocalDateTime.of(2013, 1, 7, 22, 0, 0), dateTimeRange.getStartDateTime());
    assertEquals(LocalDateTime.of(2013, 1, 8, 6, 30, 0), dateTimeRange.getEndDateTime());
  }

  @Test
  void testAllDay() {
    DateTimeRange dateTimeRange = DateTimeRange.allDay(LocalDate.of(2013, 1, 7));

    assertEquals(LocalDateTime.of(2013, 1, 7, 0, 0, 0), dateTimeRange.getStartDateTime());
    assertEquals(LocalDateTime.of(2013, 1, 8, 0, 0, 0), dateTimeRange.getEndDateTime());
  }

  @Test
  void testGetDuration() {
    LocalDateTime startDateTime = LocalDateTime.of(2013, 1, 1, 8, 0);
    LocalDateTime endDateTime = LocalDateTime.of(2013, 1, 1, 16, 0);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);

    assertEquals(28800000, dateTimeRange.getDuration().toMillis());
  }

  @Test
  void testOverlaps() {
    LocalDateTime startDateTime = LocalDateTime.of(2012, 1, 8, 0, 0, 0);
    LocalDateTime endDateTime = LocalDateTime.of(2012, 1, 14, 0, 0, 0);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);
    DateTimeRange dateTimeRangeBefore =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 7, 0, 0, 0));
    DateTimeRange dateTimeRangeAfter =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 15, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0));
    DateTimeRange dateTimeRangeOverlapStart =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 8, 0, 0, 0));
    DateTimeRange dateTimeRangeOverlapEnd =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 14, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0));
    DateTimeRange dateTimeRangeOverlapStart2 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 9, 0, 0, 0));
    DateTimeRange dateTimeRangeOverlapEnd2 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 13, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0));

    assertTrue(dateTimeRange.overlaps(dateTimeRange));
    assertFalse(dateTimeRange.overlaps(dateTimeRangeBefore));
    assertFalse(dateTimeRange.overlaps(dateTimeRangeAfter));
    assertTrue(dateTimeRange.overlaps(dateTimeRangeOverlapStart));
    assertTrue(dateTimeRange.overlaps(dateTimeRangeOverlapEnd));
    assertTrue(dateTimeRange.overlaps(dateTimeRangeOverlapStart2));
    assertTrue(dateTimeRange.overlaps(dateTimeRangeOverlapEnd2));
    assertFalse(dateTimeRange.overlaps(null));
  }

  @Test
  void testOverlapsExclusive() {
    LocalDateTime startDateTime = LocalDateTime.of(2012, 1, 8, 0, 0, 0);
    LocalDateTime endDateTime = LocalDateTime.of(2012, 1, 14, 0, 0, 0);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);
    DateTimeRange dateTimeRangeBefore =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 7, 0, 0, 0));
    DateTimeRange dateTimeRangeAfter =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 15, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0));
    DateTimeRange dateTimeRangeOverlapStart =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 8, 0, 0, 0));
    DateTimeRange dateTimeRangeOverlapEnd =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 14, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0));
    DateTimeRange dateTimeRangeOverlapStart2 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 0, 0, 0), LocalDateTime.of(2012, 1, 9, 0, 0, 0));
    DateTimeRange dateTimeRangeOverlapEnd2 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 13, 0, 0, 0), LocalDateTime.of(2012, 1, 21, 0, 0, 0));

    assertTrue(dateTimeRange.overlapsExclusive(dateTimeRange));
    assertFalse(dateTimeRange.overlapsExclusive(dateTimeRangeBefore));
    assertFalse(dateTimeRange.overlapsExclusive(dateTimeRangeAfter));
    assertFalse(dateTimeRange.overlapsExclusive(dateTimeRangeOverlapStart));
    assertFalse(dateTimeRange.overlapsExclusive(dateTimeRangeOverlapEnd));
    assertTrue(dateTimeRange.overlapsExclusive(dateTimeRangeOverlapStart2));
    assertTrue(dateTimeRange.overlapsExclusive(dateTimeRangeOverlapEnd2));
    assertFalse(dateTimeRange.overlapsExclusive(null));
  }

  static Stream<Arguments> overlapsCompletelyProvider() {
    return Stream.of(
        Arguments.of(
            DateTimeRange.of(
                LocalDateTime.of(2014, 9, 30, 17, 0), LocalDateTime.of(2014, 10, 1, 8, 59)),
            false),
        Arguments.of(
            DateTimeRange.of(
                LocalDateTime.of(2014, 9, 30, 17, 0), LocalDateTime.of(2014, 10, 1, 9, 0)),
            false),
        Arguments.of(
            DateTimeRange.of(
                LocalDateTime.of(2014, 9, 30, 17, 0), LocalDateTime.of(2014, 10, 1, 9, 1)),
            false),
        Arguments.of(
            DateTimeRange.of(
                LocalDateTime.of(2014, 10, 1, 9, 0), LocalDateTime.of(2014, 10, 1, 9, 1)),
            true),
        Arguments.of(
            DateTimeRange.of(
                LocalDateTime.of(2014, 10, 1, 9, 1), LocalDateTime.of(2014, 10, 2, 16, 59)),
            true),
        Arguments.of(
            DateTimeRange.of(
                LocalDateTime.of(2014, 10, 1, 9, 0), LocalDateTime.of(2014, 10, 2, 17, 0)),
            true),
        Arguments.of(
            DateTimeRange.of(
                LocalDateTime.of(2014, 10, 1, 9, 0), LocalDateTime.of(2014, 10, 2, 17, 1)),
            false),
        Arguments.of(
            DateTimeRange.of(
                LocalDateTime.of(2014, 10, 2, 17, 1), LocalDateTime.of(2014, 10, 2, 17, 2)),
            false));
  }

  @ParameterizedTest
  @MethodSource("overlapsCompletelyProvider")
  @DisplayName("checks for a dateTimeRange being completely contained within this dateTime range")
  void checksForADateTimeRangeBeingCompletelyContainedWithinThisDateTimeRange(
      DateTimeRange dateTimeRangeToCheck, boolean expectedResult) {
    DateTimeRange dateTimeRange =
        DateTimeRange.of(LocalDateTime.of(2014, 10, 1, 9, 0), LocalDateTime.of(2014, 10, 2, 17, 0));

    assertEquals(expectedResult, dateTimeRange.overlapsCompletely(dateTimeRangeToCheck));
  }

  @Test
  void testContainsDateTime() {
    LocalDateTime startDateTime = LocalDateTime.of(2012, 1, 8, 8, 0, 0);
    LocalDateTime endDateTime = LocalDateTime.of(2012, 1, 14, 16, 0, 0);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);

    LocalDateTime dateTimeBefore1 = LocalDateTime.of(2012, 1, 7, 0, 0, 0);
    LocalDateTime dateTimeBefore2 = LocalDateTime.of(2012, 1, 8, 7, 59, 59);
    LocalDateTime dateTimeAfter1 = LocalDateTime.of(2012, 1, 15, 0, 0, 0);
    LocalDateTime dateTimeAfter2 = LocalDateTime.of(2012, 1, 14, 16, 1, 0);

    LocalDateTime dateTimeInside1 = LocalDateTime.of(2012, 1, 8, 8, 0, 0);
    LocalDateTime dateTimeInside2 = LocalDateTime.of(2012, 1, 9, 16, 0, 0);
    LocalDateTime dateTimeInside3 = LocalDateTime.of(2012, 1, 14, 16, 0, 0);

    assertFalse(dateTimeRange.containsDateTime(null));
    assertFalse(dateTimeRange.containsDateTime(dateTimeBefore1));
    assertFalse(dateTimeRange.containsDateTime(dateTimeBefore2));
    assertFalse(dateTimeRange.containsDateTime(dateTimeAfter1));
    assertFalse(dateTimeRange.containsDateTime(dateTimeAfter2));

    assertTrue(dateTimeRange.containsDateTime(dateTimeInside1));
    assertTrue(dateTimeRange.containsDateTime(dateTimeInside2));
    assertTrue(dateTimeRange.containsDateTime(dateTimeInside3));
  }

  @Test
  void testCompareTo() {
    DateTimeRange dateTimeRange1 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 8, 8, 17, 0), LocalDateTime.of(2012, 1, 14, 8, 17, 0));
    DateTimeRange dateTimeRange2 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 8, 8, 17, 0), LocalDateTime.of(2012, 1, 14, 8, 17, 0));
    DateTimeRange dateTimeRange3 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 8, 17, 0), LocalDateTime.of(2012, 1, 7, 8, 17, 0));
    DateTimeRange dateTimeRange4 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 15, 8, 17, 0), LocalDateTime.of(2012, 1, 21, 8, 17, 0));
    DateTimeRange dateTimeRange5 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 8, 8, 17, 0), LocalDateTime.of(2012, 1, 13, 8, 17, 0));
    DateTimeRange dateTimeRange6 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 8, 8, 17, 0), LocalDateTime.of(2012, 1, 15, 8, 17, 0));

    assertEquals(0, dateTimeRange1.compareTo(dateTimeRange1));
    assertEquals(0, dateTimeRange1.compareTo(dateTimeRange2));
    assertEquals(-1, dateTimeRange1.compareTo(null));
    assertTrue(dateTimeRange1.compareTo(dateTimeRange3) >= 1);
    assertTrue(dateTimeRange1.compareTo(dateTimeRange4) < 0);
    assertTrue(dateTimeRange1.compareTo(dateTimeRange5) > 0);
    assertTrue(dateTimeRange1.compareTo(dateTimeRange6) < 0);
  }

  @Test
  void testEquals() {
    DateTimeRange dateTimeRange1 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 8, 17, 0), LocalDateTime.of(2012, 1, 7, 8, 17, 0));
    DateTimeRange dateTimeRange2 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 8, 17, 0), LocalDateTime.of(2012, 1, 7, 8, 17, 0));
    DateTimeRange dateTimeRange3 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 1, 8, 17, 0), LocalDateTime.of(2012, 1, 8, 8, 17, 0));
    DateTimeRange dateTimeRange4 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 2, 8, 17, 0), LocalDateTime.of(2012, 1, 7, 8, 17, 0));
    DateTimeRange dateTimeRange5 =
        DateTimeRange.of(
            LocalDateTime.of(2012, 1, 4, 8, 17, 0), LocalDateTime.of(2012, 1, 6, 8, 17, 0));

    assertFalse(dateTimeRange1.equals(null));
    assertFalse(dateTimeRange1.equals("ABC"));
    assertTrue(dateTimeRange1.equals(dateTimeRange1));
    assertTrue(dateTimeRange1.equals(dateTimeRange2));
    assertFalse(dateTimeRange1.equals(dateTimeRange3));
    assertFalse(dateTimeRange1.equals(dateTimeRange4));
    assertFalse(dateTimeRange1.equals(dateTimeRange5));
  }

  @Test
  void testHashCode() {
    LocalDateTime startDateTime = LocalDateTime.of(2013, 1, 1, 8, 17);
    LocalDateTime endDateTime = LocalDateTime.of(2013, 2, 1, 17, 53);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);

    int expectedHashCode = 31 * startDateTime.hashCode() + endDateTime.hashCode();

    assertEquals(expectedHashCode, dateTimeRange.hashCode());
  }

  static Stream<Arguments> containsExclusiveProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(ldt(2013, 1, 1, 8, 17), false),
        Arguments.of(ldt(2013, 1, 1, 8, 18), true),
        Arguments.of(ldt(2013, 2, 1, 17, 52), true),
        Arguments.of(ldt(2013, 2, 1, 17, 53), false));
  }

  @ParameterizedTest
  @MethodSource("containsExclusiveProvider")
  @DisplayName("containsExclusive should not include end points")
  void containsExclusiveShouldNotIncludeEndPoints(
      LocalDateTime dateTime, boolean isContainedExclusive) {
    LocalDateTime startDateTime = LocalDateTime.of(2013, 1, 1, 8, 17);
    LocalDateTime endDateTime = LocalDateTime.of(2013, 2, 1, 17, 53);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);

    assertEquals(isContainedExclusive, dateTimeRange.containsDateTimeExclusive(dateTime));
  }

  static Stream<Arguments> containsExclusiveOfEndProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(ldt(2012, 12, 31, 23, 59), false),
        Arguments.of(ldt(2013, 1, 1, 8, 16), false),
        Arguments.of(ldt(2013, 1, 1, 8, 17), true), // start inclusive
        Arguments.of(ldt(2013, 1, 1, 8, 18), true),
        Arguments.of(ldt(2013, 2, 1, 17, 52), true),
        Arguments.of(ldt(2013, 2, 1, 17, 53), false), // end exclusive
        Arguments.of(ldt(2013, 2, 1, 17, 54), false));
  }

  @ParameterizedTest
  @MethodSource("containsExclusiveOfEndProvider")
  @DisplayName("containsDateTimeExclusiveOfEndDateTime includes start and excludes end")
  void containsDateTimeExclusiveOfEndDateTimeIncludesStartAndExcludesEnd(
      LocalDateTime dateTime, boolean isContained) {
    LocalDateTime startDateTime = LocalDateTime.of(2013, 1, 1, 8, 17);
    LocalDateTime endDateTime = LocalDateTime.of(2013, 2, 1, 17, 53);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);

    assertEquals(isContained, dateTimeRange.containsDateTimeExclusiveOfEndDateTime(dateTime));
  }

  static Stream<Arguments> fractionalHoursProvider() {
    return Stream.of(
        // exact hours same day
        Arguments.of(
            LocalDateTime.of(2013, 1, 1, 8, 0, 0), LocalDateTime.of(2013, 1, 1, 16, 0, 0), 8.0d),
        // crossing midnight (8 hours)
        Arguments.of(
            LocalDateTime.of(2013, 1, 1, 22, 0, 0), LocalDateTime.of(2013, 1, 2, 6, 0, 0), 8.0d),
        // partial with minutes (90 min)
        Arguments.of(
            LocalDateTime.of(2013, 1, 1, 8, 15, 0), LocalDateTime.of(2013, 1, 1, 9, 45, 0), 1.5d),
        // second-level precision (1 hour exactly)
        Arguments.of(
            LocalDateTime.of(2013, 1, 1, 8, 0, 30), LocalDateTime.of(2013, 1, 1, 9, 0, 30), 1.0d),
        // 30 minutes 30 seconds -> 0.508333...
        Arguments.of(
            LocalDateTime.of(2013, 1, 1, 8, 0, 0),
            LocalDateTime.of(2013, 1, 1, 8, 30, 30),
            (30 * 60 + 30) / 3600.0d),
        // zero duration
        Arguments.of(
            LocalDateTime.of(2013, 1, 1, 8, 0, 0), LocalDateTime.of(2013, 1, 1, 8, 0, 0), 0.0d));
  }

  @ParameterizedTest
  @MethodSource("fractionalHoursProvider")
  @DisplayName("getFractionalHours computes expected fractional hours")
  void getFractionalHoursComputesExpectedFractionalHours(
      LocalDateTime start, LocalDateTime end, double expected) {
    assertTrue(Math.abs(DateTimeRange.of(start, end).getFractionalHours() - expected) < 1e-9);
  }

  static Stream<Arguments> overlapRangeProvider() {
    return Stream.of(
        Arguments.of(null, null),
        Arguments.of(DateTimeRange.of(ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 24)), null),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 25)),
            DateTimeRange.of(ldt(2014, 9, 23, 9, 25), ldt(2014, 9, 23, 9, 25))),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 9, 20), ldt(2014, 9, 23, 10, 0)),
            DateTimeRange.of(ldt(2014, 9, 23, 9, 25), ldt(2014, 9, 23, 10, 0))),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 12, 0), ldt(2014, 9, 23, 13, 0)),
            DateTimeRange.of(ldt(2014, 9, 23, 12, 0), ldt(2014, 9, 23, 13, 0))),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 17, 40), ldt(2014, 9, 23, 17, 50)),
            DateTimeRange.of(ldt(2014, 9, 23, 17, 40), ldt(2014, 9, 23, 17, 45))),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 18, 0)),
            DateTimeRange.of(ldt(2014, 9, 23, 9, 25), ldt(2014, 9, 23, 17, 45))));
  }

  @ParameterizedTest
  @MethodSource("overlapRangeProvider")
  @DisplayName("overlap of two DateTimeRanges should have a DateTimeRange itself")
  void overlapOfTwoDateTimeRangesShouldHaveADateTimeRangeItself(
      DateTimeRange otherDateTimeRange, DateTimeRange overlappingRange) {
    LocalDateTime startDateTime = LocalDateTime.of(2014, 9, 23, 9, 25);
    LocalDateTime endDateTime = LocalDateTime.of(2014, 9, 23, 17, 45);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);

    assertEquals(overlappingRange, dateTimeRange.overlapRange(otherDateTimeRange));
  }

  static Stream<Arguments> overlapDurationProvider() {
    return Stream.of(
        Arguments.of(null, Duration.ofMinutes(0)),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 25)),
            Duration.ofMinutes(0)),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 9, 20), ldt(2014, 9, 23, 10, 0)),
            Duration.ofMinutes(35)),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 11, 0), ldt(2014, 9, 23, 12, 0)),
            Duration.ofHours(1)),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 17, 44), ldt(2014, 9, 23, 17, 46)),
            Duration.ofMinutes(1)),
        Arguments.of(
            DateTimeRange.of(ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 18, 0)),
            Duration.ofMinutes(500)));
  }

  @ParameterizedTest
  @MethodSource("overlapDurationProvider")
  @DisplayName("overlap of two DateTimeRanges should have some duration")
  void overlapOfTwoDateTimeRangesShouldHaveSomeDuration(
      DateTimeRange otherDateTimeRange, Duration duration) {
    LocalDateTime startDateTime = LocalDateTime.of(2014, 9, 23, 9, 25);
    LocalDateTime endDateTime = LocalDateTime.of(2014, 9, 23, 17, 45);
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);

    assertEquals(duration, dateTimeRange.overlapDuration(otherDateTimeRange));
  }

  private static LocalDateTime ldt(int year, int month, int day, int hour, int minute) {
    return LocalDateTime.of(year, month, day, hour, minute);
  }
}
