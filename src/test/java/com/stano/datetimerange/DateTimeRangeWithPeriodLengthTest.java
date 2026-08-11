package com.stano.datetimerange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateTimeRangeWithPeriodLengthTest {

  static Stream<Arguments> numberOfPeriodsInRangeProvider() {
    return Stream.of(
        // exact multiples within same day (8 hours = 480 minutes)
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 60, 8),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 30, 16),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 15, 32),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 10, 48),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 5, 96),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 1, 480),
        // crossing midnight (also 8 hours)
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 60, 8),
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 30, 16),
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 15, 32),
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 10, 48),
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 5, 96),
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 1, 480),
        // non-exact multiple (70 minutes / 15 = 4 periods)
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 9, 10), 15, 4),
        // shorter than one period -> 0
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 8, 10), 15, 0));
  }

  @ParameterizedTest
  @MethodSource("numberOfPeriodsInRangeProvider")
  void testGetNumberOfPeriodsInRange(
      LocalDateTime startDateTime, LocalDateTime endDateTime, int periodLength, int expected) {
    assertEquals(
        expected,
        DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, periodLength)
            .getNumberOfPeriodsInRange());
  }

  @Test
  void testGetters() {
    LocalDateTime startDateTime = localDateTime(2013, 1, 1, 8, 0);
    LocalDateTime endDateTime = localDateTime(2013, 1, 1, 16, 0);
    int periodLength = 60;
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);
    DateTimeRangeWithPeriodLength dateTimeRangeWithPeriod =
        DateTimeRangeWithPeriodLength.of(dateTimeRange, periodLength);

    assertEquals(dateTimeRange, dateTimeRangeWithPeriod.getDateTimeRange());
    assertEquals(60, dateTimeRangeWithPeriod.getPeriodLengthInMinutes());
  }

  static Stream<Arguments> startingAndEndingIndexProvider() {
    return Stream.of(
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 60, 8, 16),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 30, 16, 32),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 15, 32, 64),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 10, 48, 96),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 5, 96, 192),
        Arguments.of(
            localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 1, 480, 960),
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 60, 22, 30),
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 30, 44, 60),
        Arguments.of(
            localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 15, 88, 120),
        Arguments.of(
            localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 10, 132, 180),
        Arguments.of(
            localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 5, 264, 360),
        Arguments.of(
            localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 1, 1320, 1800));
  }

  @ParameterizedTest
  @MethodSource("startingAndEndingIndexProvider")
  void testGetStartingAndEndingIndex(
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      int periodLength,
      int expectedStartIndex,
      int expectedEndIndex) {
    DateTimeRange dateTimeRange = DateTimeRange.of(startDateTime, endDateTime);
    DateTimeRangeWithPeriodLength dateTimeRangeWithPeriod =
        DateTimeRangeWithPeriodLength.of(dateTimeRange, periodLength);

    assertEquals(expectedStartIndex, dateTimeRangeWithPeriod.getStartIndex());
    assertEquals(expectedEndIndex, dateTimeRangeWithPeriod.getEndIndex());
  }

  @Test
  void testEquals() {
    DateTimeRangeWithPeriodLength dateTimeRange1 =
        DateTimeRangeWithPeriodLength.of(
            localDateTime(2012, 1, 1, 8, 17), localDateTime(2012, 1, 7, 8, 17), 30);
    DateTimeRangeWithPeriodLength dateTimeRange2 =
        DateTimeRangeWithPeriodLength.of(
            localDateTime(2012, 1, 1, 8, 17), localDateTime(2012, 1, 7, 8, 17), 30);
    DateTimeRangeWithPeriodLength dateTimeRange3 =
        DateTimeRangeWithPeriodLength.of(
            localDateTime(2012, 1, 1, 8, 17), localDateTime(2012, 1, 8, 8, 17), 30);
    DateTimeRangeWithPeriodLength dateTimeRange4 =
        DateTimeRangeWithPeriodLength.of(
            localDateTime(2012, 1, 2, 8, 17), localDateTime(2012, 1, 7, 8, 17), 30);
    DateTimeRangeWithPeriodLength dateTimeRange5 =
        DateTimeRangeWithPeriodLength.of(
            localDateTime(2012, 1, 4, 8, 17), localDateTime(2012, 1, 6, 8, 17), 30);

    assertEquals(false, dateTimeRange1.equals(null));
    assertEquals(false, dateTimeRange1.equals("ABC"));
    assertEquals(true, dateTimeRange1.equals(dateTimeRange1));
    assertEquals(true, dateTimeRange1.equals(dateTimeRange2));
    assertEquals(false, dateTimeRange1.equals(dateTimeRange3));
    assertEquals(false, dateTimeRange1.equals(dateTimeRange4));
    assertEquals(false, dateTimeRange1.equals(dateTimeRange5));
  }

  @Test
  void testHashCode() {
    LocalDateTime startDateTime = localDateTime(2013, 1, 1, 8, 17);
    LocalDateTime endDateTime = localDateTime(2013, 2, 1, 17, 53);
    DateTimeRangeWithPeriodLength dateTimeRange =
        DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, 30);

    int expectedHashCode = 31 * startDateTime.hashCode() + endDateTime.hashCode();

    assertEquals(expectedHashCode, dateTimeRange.hashCode());
  }

  @Test
  void testIterator() {
    LocalDateTime startDateTime = localDateTime(2013, 1, 1, 8, 0);
    LocalDateTime endDateTime = localDateTime(2013, 1, 1, 16, 0);
    DateTimeRangeWithPeriodLength dateTimeRange =
        DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, 30);

    List<LocalDateTime> expected =
        List.of(
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
            localDateTime(2013, 1, 1, 15, 30));

    List<LocalDateTime> results = new ArrayList<>();
    for (LocalDateTime dateTime : dateTimeRange) {
      results.add(dateTime);
    }

    assertEquals(expected, results);
  }

  @Test
  void testIteratorRemove() {
    LocalDateTime startDateTime = localDateTime(2013, 1, 1, 8, 0);
    LocalDateTime endDateTime = localDateTime(2013, 1, 1, 16, 0);
    DateTimeRangeWithPeriodLength dateTimeRange =
        DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, 30);

    assertThrows(UnsupportedOperationException.class, () -> dateTimeRange.iterator().remove());
  }

  static Stream<Arguments> indexRangeProvider() {
    return Stream.of(
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 60, 8, 15),
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 60, 22, 29),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 30, 16, 31),
        Arguments.of(localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 30, 44, 59),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 15, 32, 63),
        Arguments.of(
            localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 15, 88, 119),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 10, 48, 95),
        Arguments.of(
            localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 10, 132, 179),
        Arguments.of(localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 5, 96, 191),
        Arguments.of(
            localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 5, 264, 359),
        Arguments.of(
            localDateTime(2013, 1, 1, 8, 0), localDateTime(2013, 1, 1, 16, 0), 1, 480, 959),
        Arguments.of(
            localDateTime(2013, 1, 1, 22, 0), localDateTime(2013, 1, 2, 6, 0), 1, 1320, 1799));
  }

  @ParameterizedTest
  @MethodSource("indexRangeProvider")
  void testGetIndexRange(
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      int periodLength,
      int startIndex,
      int endIndex) {
    DateTimeRangeWithPeriodLength dateTimeRange =
        DateTimeRangeWithPeriodLength.of(startDateTime, endDateTime, periodLength);
    List<Integer> indexes = dateTimeRange.getIndexStream().boxed().toList();

    assertEquals(startIndex, indexes.get(0));
    assertEquals(endIndex, indexes.get(indexes.size() - 1));
  }

  static LocalDateTime localDateTime(int year, int month, int day, int hour, int minute) {
    return LocalDateTime.of(year, month, day, hour, minute);
  }
}
