package com.stano.datetime;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link JavaTimeUtil}'s conversions between {@code java.time} types and {@code
 * java.sql.*} types.
 *
 * <p>Kept separate from {@link JavaTimeUtilTest} so this file can plainly import {@code
 * java.sql.Date} without colliding with {@code java.util.Date}, which {@link JavaTimeUtilTest} uses
 * for the {@code java.util.Date} conversions.
 */
class JavaTimeUtilSqlConversionsTest {

  static Stream<Arguments> sqlDateToLocalDateProvider() {
    return Stream.of(
        Arguments.of(makeSqlDate(2013, 1, 17), 2013, Month.JANUARY, 17),
        Arguments.of(makeSqlDate(2013, 2, 17), 2013, Month.FEBRUARY, 17),
        Arguments.of(makeSqlDate(2013, 3, 17), 2013, Month.MARCH, 17),
        Arguments.of(makeSqlDate(2013, 4, 17), 2013, Month.APRIL, 17),
        Arguments.of(makeSqlDate(2013, 5, 17), 2013, Month.MAY, 17),
        Arguments.of(makeSqlDate(2013, 6, 17), 2013, Month.JUNE, 17),
        Arguments.of(makeSqlDate(2013, 7, 17), 2013, Month.JULY, 17),
        Arguments.of(makeSqlDate(2013, 8, 17), 2013, Month.AUGUST, 17),
        Arguments.of(makeSqlDate(2013, 9, 17), 2013, Month.SEPTEMBER, 17),
        Arguments.of(makeSqlDate(2013, 10, 17), 2013, Month.OCTOBER, 17),
        Arguments.of(makeSqlDate(2013, 11, 17), 2013, Month.NOVEMBER, 17),
        Arguments.of(makeSqlDate(2013, 12, 17), 2013, Month.DECEMBER, 17));
  }

  @ParameterizedTest
  @MethodSource("sqlDateToLocalDateProvider")
  void testSqlDateToLocalDate(Date sqlDate, int year, Month month, int dayOfMonth) {
    LocalDate localDate = JavaTimeUtil.sqlDateToLocalDate(sqlDate);

    assertEquals(year, localDate.getYear());
    assertEquals(month, localDate.getMonth());
    assertEquals(dayOfMonth, localDate.getDayOfMonth());
  }

  static Stream<Arguments> sqlTimeToLocalTimeProvider() {
    return Stream.of(
        Arguments.of(makeSqlTime(0, 0, 0), 0, 0, 0),
        Arguments.of(makeSqlTime(8, 0, 0), 8, 0, 0),
        Arguments.of(makeSqlTime(16, 47, 0), 16, 47, 0),
        Arguments.of(makeSqlTime(18, 30, 45), 18, 30, 45));
  }

  @ParameterizedTest
  @MethodSource("sqlTimeToLocalTimeProvider")
  void testSqlTimeToLocalTime(Time sqlTime, int hour, int minute, int second) {
    LocalTime localTime = JavaTimeUtil.sqlTimeToLocalTime(sqlTime);

    assertEquals(hour, localTime.getHour());
    assertEquals(minute, localTime.getMinute());
    assertEquals(second, localTime.getSecond());
  }

  static Stream<Arguments> sqlTimestampToLocalDateTimeProvider() {
    return Stream.of(
        Arguments.of(makeSqlTimestamp(2013, 3, 17, 0, 0, 0), 2013, Month.MARCH, 17, 0, 0, 0),
        Arguments.of(makeSqlTimestamp(2013, 3, 17, 8, 0, 0), 2013, Month.MARCH, 17, 8, 0, 0),
        Arguments.of(makeSqlTimestamp(2013, 3, 17, 16, 47, 0), 2013, Month.MARCH, 17, 16, 47, 0),
        Arguments.of(makeSqlTimestamp(2013, 3, 17, 18, 30, 45), 2013, Month.MARCH, 17, 18, 30, 45));
  }

  @ParameterizedTest
  @MethodSource("sqlTimestampToLocalDateTimeProvider")
  void testSqlTimestampToLocalDateTime(
      Timestamp sqlTimestamp,
      int year,
      Month month,
      int dayOfMonth,
      int hour,
      int minute,
      int second) {
    LocalDateTime localDateTime = JavaTimeUtil.sqlTimestampToLocalDateTime(sqlTimestamp);

    assertEquals(year, localDateTime.getYear());
    assertEquals(month, localDateTime.getMonth());
    assertEquals(dayOfMonth, localDateTime.getDayOfMonth());
    assertEquals(hour, localDateTime.getHour());
    assertEquals(minute, localDateTime.getMinute());
    assertEquals(second, localDateTime.getSecond());
  }

  static Stream<Arguments> localDateToSqlDateProvider() {
    return Stream.of(
        Arguments.of(LocalDate.of(1970, Month.JANUARY, 1), makeSqlDate(1970, 1, 1)),
        Arguments.of(LocalDate.of(2013, Month.MARCH, 17), makeSqlDate(2013, 3, 17)),
        Arguments.of(LocalDate.of(2014, Month.JANUARY, 1), makeSqlDate(2014, 1, 1)));
  }

  @ParameterizedTest
  @MethodSource("localDateToSqlDateProvider")
  void testLocalDateToSqlDate(LocalDate localDate, Date sqlDate) {
    assertEquals(sqlDate.getTime(), JavaTimeUtil.localDateToSqlDate(localDate).getTime());
  }

  static Stream<Arguments> localTimeToSqlTimeProvider() {
    return Stream.of(
        Arguments.of(LocalTime.of(0, 0, 0), makeSqlTime(0, 0, 0)),
        Arguments.of(LocalTime.of(8, 0, 0), makeSqlTime(8, 0, 0)),
        Arguments.of(LocalTime.of(16, 47, 0), makeSqlTime(16, 47, 0)),
        Arguments.of(LocalTime.of(18, 30, 45), makeSqlTime(18, 30, 45)));
  }

  @ParameterizedTest
  @MethodSource("localTimeToSqlTimeProvider")
  void testLocalTimeToSqlTime(LocalTime localTime, Time sqlTime) {
    assertEquals(sqlTime.getTime(), JavaTimeUtil.localTimeToSqlTime(localTime).getTime());
  }

  static Stream<Arguments> localDateTimeToSqlTimestampProvider() {
    return Stream.of(
        Arguments.of(
            LocalDateTime.of(2013, Month.MARCH, 17, 0, 0, 0),
            makeSqlTimestamp(2013, 3, 17, 0, 0, 0)),
        Arguments.of(
            LocalDateTime.of(2013, Month.MARCH, 17, 8, 0, 0),
            makeSqlTimestamp(2013, 3, 17, 8, 0, 0)),
        Arguments.of(
            LocalDateTime.of(2013, Month.MARCH, 17, 16, 47, 0),
            makeSqlTimestamp(2013, 3, 17, 16, 47, 0)),
        Arguments.of(
            LocalDateTime.of(2013, Month.MARCH, 17, 18, 30, 45),
            makeSqlTimestamp(2013, 3, 17, 18, 30, 45)));
  }

  @ParameterizedTest
  @MethodSource("localDateTimeToSqlTimestampProvider")
  void testLocalDateTimeToSqlTimestamp(LocalDateTime localDateTime, Timestamp sqlTimestamp) {
    assertEquals(
        sqlTimestamp.getTime(), JavaTimeUtil.localDateTimeToSqlTimestamp(localDateTime).getTime());
  }

  private static Date makeSqlDate(int year, int month, int day) {
    return new Date(JavaTimeUtilTest.makeDate(year, month, day, 0, 0, 0).getTime());
  }

  private static Time makeSqlTime(int hour, int minute, int second) {
    return new Time(JavaTimeUtilTest.makeDate(1970, 1, 1, hour, minute, second).getTime());
  }

  private static Timestamp makeSqlTimestamp(
      int year, int month, int day, int hour, int minute, int second) {
    return new Timestamp(
        JavaTimeUtilTest.makeDate(year, month, day, hour, minute, second).getTime());
  }
}
