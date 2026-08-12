package com.stano.datetime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JavaTimeUtilTest {

  static Stream<Arguments> javaDateToLocalDateProvider() {
    return Stream.of(
        Arguments.of(makeDate(2013, 3, 17, 0, 0, 0), 2013, Month.MARCH, 17),
        Arguments.of(makeDate(2013, 3, 17, 8, 0, 0), 2013, Month.MARCH, 17),
        Arguments.of(makeDate(2013, 3, 17, 16, 47, 0), 2013, Month.MARCH, 17),
        Arguments.of(makeDate(2013, 3, 17, 18, 30, 45), 2013, Month.MARCH, 17),
        Arguments.of(makeDate(2013, 3, 17, 23, 59, 59), 2013, Month.MARCH, 17));
  }

  @ParameterizedTest
  @MethodSource("javaDateToLocalDateProvider")
  void testJavaDateToLocalDate(Date javaDate, int year, Month month, int dayOfMonth) {
    LocalDate localDate = JavaTimeUtil.javaDateToLocalDate(javaDate);

    assertEquals(year, localDate.getYear());
    assertEquals(month, localDate.getMonth());
    assertEquals(dayOfMonth, localDate.getDayOfMonth());
  }

  static Stream<Arguments> javaDateToLocalDateTimeProvider() {
    return Stream.of(
        Arguments.of(makeDate(2013, 3, 17, 0, 0, 0), 2013, Month.MARCH, 17, 0, 0, 0),
        Arguments.of(makeDate(2013, 3, 17, 8, 0, 0), 2013, Month.MARCH, 17, 8, 0, 0),
        Arguments.of(makeDate(2013, 3, 17, 16, 47, 0), 2013, Month.MARCH, 17, 16, 47, 0),
        Arguments.of(makeDate(2013, 3, 17, 18, 30, 45), 2013, Month.MARCH, 17, 18, 30, 45));
  }

  @ParameterizedTest
  @MethodSource("javaDateToLocalDateTimeProvider")
  void testJavaDateToLocalDateTime(
      Date javaDate, int year, Month month, int dayOfMonth, int hour, int minute, int second) {
    LocalDateTime localDateTime = JavaTimeUtil.javaDateToLocalDateTime(javaDate);

    assertEquals(year, localDateTime.getYear());
    assertEquals(month, localDateTime.getMonth());
    assertEquals(dayOfMonth, localDateTime.getDayOfMonth());
    assertEquals(hour, localDateTime.getHour());
    assertEquals(minute, localDateTime.getMinute());
    assertEquals(second, localDateTime.getSecond());
  }

  static Stream<Arguments> javaDateToLocalTimeProvider() {
    return Stream.of(
        Arguments.of(makeDate(2013, 3, 17, 0, 0, 0), 0, 0, 0),
        Arguments.of(makeDate(2013, 3, 17, 8, 0, 0), 8, 0, 0),
        Arguments.of(makeDate(2013, 3, 17, 16, 47, 0), 16, 47, 0),
        Arguments.of(makeDate(2013, 3, 17, 18, 30, 45), 18, 30, 45));
  }

  @ParameterizedTest
  @MethodSource("javaDateToLocalTimeProvider")
  void testJavaDateToLocalTime(Date javaDate, int hour, int minute, int second) {
    LocalTime localTime = JavaTimeUtil.javaDateToLocalTime(javaDate);

    assertEquals(hour, localTime.getHour());
    assertEquals(minute, localTime.getMinute());
    assertEquals(second, localTime.getSecond());
  }

  static Stream<Arguments> localDateToJavaDateProvider() {
    return Stream.of(
        Arguments.of(LocalDate.of(1970, Month.JANUARY, 1), makeDate(1970, 1, 1, 0, 0, 0)),
        Arguments.of(LocalDate.of(2013, Month.MARCH, 17), makeDate(2013, 3, 17, 0, 0, 0)));
  }

  @ParameterizedTest
  @MethodSource("localDateToJavaDateProvider")
  void testLocalDateToJavaDate(LocalDate localDate, Date javaDate) {
    assertEquals(javaDate.getTime(), JavaTimeUtil.localDateToJavaDate(localDate).getTime());
  }

  static Stream<Arguments> localDateTimeToDateProvider() {
    return Stream.of(
        Arguments.of(
            LocalDateTime.of(2013, Month.MARCH, 17, 0, 0, 0), makeDate(2013, 3, 17, 0, 0, 0)),
        Arguments.of(
            LocalDateTime.of(2013, Month.MARCH, 17, 8, 0, 0), makeDate(2013, 3, 17, 8, 0, 0)),
        Arguments.of(
            LocalDateTime.of(2013, Month.MARCH, 17, 16, 47, 0), makeDate(2013, 3, 17, 16, 47, 0)),
        Arguments.of(
            LocalDateTime.of(2013, Month.MARCH, 17, 18, 30, 45),
            makeDate(2013, 3, 17, 18, 30, 45)));
  }

  @ParameterizedTest
  @MethodSource("localDateTimeToDateProvider")
  void testLocalDateTimeToDate(LocalDateTime localDateTime, Date javaDate) {
    assertEquals(javaDate.getTime(), JavaTimeUtil.localDateTimeToJavaDate(localDateTime).getTime());
  }

  @Test
  void testUsDayNumberToDayOfWeek() {
    assertEquals(DayOfWeek.SUNDAY, JavaTimeUtil.usDayNumberToDayOfWeek(1));
    assertEquals(DayOfWeek.MONDAY, JavaTimeUtil.usDayNumberToDayOfWeek(2));
    assertEquals(DayOfWeek.TUESDAY, JavaTimeUtil.usDayNumberToDayOfWeek(3));
    assertEquals(DayOfWeek.WEDNESDAY, JavaTimeUtil.usDayNumberToDayOfWeek(4));
    assertEquals(DayOfWeek.THURSDAY, JavaTimeUtil.usDayNumberToDayOfWeek(5));
    assertEquals(DayOfWeek.FRIDAY, JavaTimeUtil.usDayNumberToDayOfWeek(6));
    assertEquals(DayOfWeek.SATURDAY, JavaTimeUtil.usDayNumberToDayOfWeek(7));
  }

  @Test
  void testUsDayNumberToDayOfWeekInvalidNumber() {
    assertThrows(IllegalArgumentException.class, () -> JavaTimeUtil.usDayNumberToDayOfWeek(8));
  }

  @Test
  void testDayOfWeekToUsDayNumber() {
    assertEquals(1, JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.SUNDAY));
    assertEquals(2, JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.MONDAY));
    assertEquals(3, JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.TUESDAY));
    assertEquals(4, JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.WEDNESDAY));
    assertEquals(5, JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.THURSDAY));
    assertEquals(6, JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.FRIDAY));
    assertEquals(7, JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.SATURDAY));
  }

  @Test
  void testGetUTCCalender() {
    assertEquals(TimeZone.getTimeZone("UTC"), JavaTimeUtil.getUTCCalender().getTimeZone());
  }

  @Test
  @DisplayName("toLocalDateTimeAtZone")
  void toLocalDateTimeAtZone() {
    LocalDateTime utcDateTime = LocalDateTime.of(1993, 3, 26, 8, 30, 45);
    LocalDateTime nyDateTime = LocalDateTime.of(1993, 3, 26, 3, 30, 45);

    assertEquals(
        nyDateTime,
        JavaTimeUtil.toLocalDateTimeAtZone(
            utcDateTime, ZoneId.of("UTC"), ZoneId.of("America/New_York")));
  }

  @Test
  void testPrivateConstructorSoCoverageIs100Percent() throws ReflectiveOperationException {
    Constructor<JavaTimeUtil> constructor = JavaTimeUtil.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    assertNotNull(constructor.newInstance());
  }

  static Date makeDate(int year, int month, int day, int hour, int minute, int second) {
    return new Date(Date.UTC(year - 1900, month - 1, day, hour, minute, second));
  }
}
